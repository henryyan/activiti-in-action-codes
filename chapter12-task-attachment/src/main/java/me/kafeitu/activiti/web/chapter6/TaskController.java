package me.kafeitu.activiti.web.chapter6;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 任务控制器
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter6")
public class TaskController {

    private static String TASK_LIST = "redirect:/chapter6/task/list";

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    IdentityService identityService;

    @Autowired
    HistoryService historyService;

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "task/list")
    public ModelAndView todoTasks(HttpSession session) throws Exception {
        String viewName = "chapter6/task-list";
        ModelAndView mav = new ModelAndView(viewName);
        User user = UserUtil.getUserFromSession(session);

        /*// 读取直接分配给当前人或者已经签收的任务
        List<Task> doingTasks = taskService.createTaskQuery().taskAssignee(user.getId()).list();

        // 等待签收的任务--废弃，用taskInvolvedUser代替
        // List<Task> waitingClaimTasks =
        // taskService.createTaskQuery().taskCandidateUser(user.getId()).list();

        // 受邀任务
        List<Task> involvedTasks = taskService.createTaskQuery().taskInvolvedUser(user.getId()).list();

        // 合并两种任务
        List<Task> allTasks = new ArrayList<Task>();
        allTasks.addAll(doingTasks);
        // allTasks.addAll(waitingClaimTasks);
        allTasks.addAll(involvedTasks);*/

        // 5.16版本可以使用一下代码待办查询
        List<Task> allTasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();

        mav.addObject("tasks", allTasks);
        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, @RequestParam(value = "nextDo", required = false) String nextDo, HttpSession session,
                        RedirectAttributes redirectAttributes) {
        String userId = UserUtil.getUserFromSession(session).getId();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        if (StringUtils.equals(nextDo, "handle")) {
            return "redirect:/chapter6/task/getform/" + taskId;
        } else {
            return TASK_LIST;
        }
    }

    /**
     * 反签收任务
     */
    @RequestMapping(value = "task/unclaim/{id}")
    public String unclaim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        // 反签收条件过滤
        List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : links) {
            // 如果一个任务有相关的候选人、组就可以反签收
            if (StringUtils.equals(IdentityLinkType.CANDIDATE, identityLink.getType())) {
                taskService.claim(taskId, null);
                redirectAttributes.addFlashAttribute("message", "任务已反签收");
                return TASK_LIST;
            }
        }
        redirectAttributes.addFlashAttribute("error", "该任务不允许反签收！");
        return TASK_LIST;
    }

    /**
     * 读取用户任务的表单字段
     */
    @RequestMapping(value = "task/getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
        String viewName = "chapter6/task-form";
        ModelAndView mav = new ModelAndView(viewName);
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Task task = null;

        // 外置表单
        if (taskFormData != null && taskFormData.getFormKey() != null) {
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("taskFormData", renderedTaskForm);
            mav.addObject("hasFormKey", true);
        } else if (taskFormData != null) { // 动态表单
            mav.addObject("taskFormData", taskFormData);
            task = taskFormData.getTask();
        } else { // 手动创建的任务（包括子任务）
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("manualTask", true);
        }
        mav.addObject("task", task);

        // 读取任务参与人列表
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        mav.addObject("identityLinksForTask", identityLinksForTask);

        // 读取所有人员
        List<User> users = identityService.createUserQuery().list();
        mav.addObject("users", users);

        // 读取所有组
        List<Group> groups = identityService.createGroupQuery().list();
        mav.addObject("groups", groups);

        // 读取子任务
        List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();
        mav.addObject("subTasks", subTasks);

        // 读取上级任务
        if (task != null && task.getParentTaskId() != null) {
            HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
            mav.addObject("parentTask", parentTask);
        }

        // 读取附件
        List<Attachment> attachments = null;
        if (task.getTaskDefinitionKey() != null) {
            attachments = taskService.getTaskAttachments(taskId);
        } else {
            attachments = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
        }
        mav.addObject("attachments", attachments);

        return mav;
    }

    /**
     * 查看已结束任务
     */
    @RequestMapping(value = "task/archived/{taskId}")
    public ModelAndView viewHistoryTask(@PathVariable("taskId") String taskId) throws Exception {
        String viewName = "chapter6/task-form-archived";
        ModelAndView mav = new ModelAndView(viewName);
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task.getParentTaskId() != null) {
            HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
            mav.addObject("parentTask", parentTask);
        }
        mav.addObject("task", task);

        // 读取子任务
        List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();
        mav.addObject("subTasks", subTasks);

        // 读取附件
        List<Attachment> attachments = null;
        if (task.getTaskDefinitionKey() != null) {
            attachments = taskService.getTaskAttachments(taskId);
        } else {
            attachments = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
        }
        mav.addObject("attachments", attachments);

        return mav;
    }

    /**
     * 读取启动流程的表单字段
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "task/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {

        // 设置当前操作人，对于调用活动可以获取到当前操作人
        String currentUserId = UserUtil.getUserFromSession(request.getSession()).getId();
        identityService.setAuthenticatedUserId(currentUserId);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 如果任务的流程定义任务Key为空则认为是手动创建的任务
        if (StringUtils.isBlank(task.getTaskDefinitionKey())) {
            taskService.complete(taskId);
            return TASK_LIST;
        }

        // 权限检查-任务的办理人和当前人不一致不能完成任务
        if (!task.getAssignee().equals(currentUserId)) {
            redirectAttributes.addFlashAttribute("error", "没有权限，不能完成该任务！");
            return "redirect:/chapter6/task/getform/" + taskId;
        }

        // 单独处理被委派的任务
        if (task.getDelegationState() == DelegationState.PENDING) {
            taskService.resolveTask(taskId);
            return TASK_LIST;
        }

        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();
        // 从请求中获取表单字段的值
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, String> formValues = new HashMap<String, String>();

        if (StringUtils.isNotBlank(formKey)) { // formkey表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        } else { // 动态表单
            for (FormProperty formProperty : formProperties) {
                if (formProperty.isWritable()) {
                    String value = request.getParameter(formProperty.getId());
                    formValues.put(formProperty.getId(), value);
                }
            }
        }
        formService.submitTaskFormData(taskId, formValues);
        return TASK_LIST;
    }

    /**
     * 更改任务属性
     *
     * @throws ParseException
     */
    @RequestMapping("task/property/{taskId}")
    @ResponseBody
    public String changeTaskProperty(@PathVariable("taskId") String taskId, @RequestParam("propertyName") String propertyName, @RequestParam("value") String value)
            throws ParseException {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 更改到期日
        if (StringUtils.equals(propertyName, "dueDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = sdf.parse(value);
            task.setDueDate(parse);
            taskService.saveTask(task);
        } else if (StringUtils.equals(propertyName, "priority")) {
            // 更改任务优先级
            task.setPriority(Integer.parseInt(value));
            taskService.saveTask(task);
        } else if (StringUtils.equals(propertyName, "owner")) {
            // 更改拥有人
            task.setOwner(value);
            taskService.saveTask(task);
        } else if (StringUtils.equals(propertyName, "assignee")) {
            // 更改办理人
            task.setAssignee(value);
            taskService.saveTask(task);
        } else {
            return "不支持[" + propertyName + "]属性！";
        }
        return "success";
    }

    /**
     * 添加参与人
     */
    @RequestMapping("task/participant/add/{taskId}")
    @ResponseBody
    public String addParticipants(@PathVariable("taskId") String taskId, @RequestParam("userId[]") String[] userIds, @RequestParam("type[]") String[] types,
                                  HttpServletRequest request) {
        // 设置当前操作人，对于调用活动可以获取到当前操作人
        String currentUserId = UserUtil.getUserFromSession(request.getSession()).getId();
        identityService.setAuthenticatedUserId(currentUserId);

        for (int i = 0; i < userIds.length; i++) {
            taskService.addUserIdentityLink(taskId, userIds[i], types[i]);
        }
        return "success";
    }

    /**
     * 删除参与人
     */
    @RequestMapping("task/participant/delete/{taskId}")
    @ResponseBody
    public String deleteParticipant(@PathVariable("taskId") String taskId, @RequestParam(value = "userId", required = false) String userId,
                                    @RequestParam(value = "groupId", required = false) String groupId, @RequestParam("type") String type) {
    /*
     * 区分用户、组，使用不同的处理方式
     */
        if (StringUtils.isNotBlank(groupId)) {
            taskService.deleteCandidateGroup(taskId, groupId);
        } else {
            taskService.deleteUserIdentityLink(taskId, userId, type);
        }
        return "success";
    }

    /**
     * 添加候选人
     */
    @RequestMapping("task/candidate/add/{taskId}")
    @ResponseBody
    public String addCandidates(@PathVariable("taskId") String taskId, @RequestParam("userOrGroupIds[]") String[] userOrGroupIds,
                                @RequestParam("type[]") String[] types, HttpServletRequest request) {
        // 设置当前操作人，对于调用活动可以获取到当前操作人
        String currentUserId = UserUtil.getUserFromSession(request.getSession()).getId();
        identityService.setAuthenticatedUserId(currentUserId);

        for (int i = 0; i < userOrGroupIds.length; i++) {
            String type = types[i];
            if (StringUtils.equals("user", type)) {
                taskService.addCandidateUser(taskId, userOrGroupIds[i]);
            } else if (StringUtils.equals("group", type)) {
                taskService.addCandidateGroup(taskId, userOrGroupIds[i]);
            }
        }
        return "success";
    }

    /**
     * 添加子任务
     */
    @RequestMapping("task/subtask/add/{taskId}")
    public String addSubTask(@PathVariable("taskId") String parentTaskId, @RequestParam("taskName") String taskName,
                             @RequestParam(value = "description", required = false) String description, HttpSession session) {
        Task newTask = taskService.newTask();
        newTask.setParentTaskId(parentTaskId);
        String userId = UserUtil.getUserFromSession(session).getId();
        newTask.setOwner(userId);
        newTask.setAssignee(userId);
        newTask.setName(taskName);
        newTask.setDescription(description);

        taskService.saveTask(newTask);
        return "redirect:/chapter6/task/getform/" + parentTaskId;
    }

    /**
     * 删除子任务
     */
    @RequestMapping("task/delete/{taskId}")
    public String deleteSubTask(@PathVariable("taskId") String taskId, HttpSession session) {
        String userId = UserUtil.getUserFromSession(session).getId();
        taskService.deleteTask(taskId, "deleteByUser" + userId);
        return "redirect:/chapter6/task/archived/" + taskId;
    }

    /**
     * 新任务
     */
    @RequestMapping("task/new")
    public String newTask(@RequestParam("taskName") String taskName, @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "priority", required = false) int priority, @RequestParam(value = "dueDate", required = false) String dueDate,
                          HttpSession session) {
        Task newTask = taskService.newTask();
        String userId = UserUtil.getUserFromSession(session).getId();
        newTask.setOwner(userId);
        newTask.setAssignee(userId);
        newTask.setName(taskName);
        newTask.setDescription(description);
        if (StringUtils.isNotBlank(dueDate)) {
            try {
                newTask.setDueDate(java.sql.Date.valueOf(dueDate));
            } catch (Exception e) {
            }
        }
        newTask.setPriority(priority);

        taskService.saveTask(newTask);
        return "redirect:/chapter6/task/getform/" + newTask.getId();
    }

    /**
     * 任务委派
     *
     * @param taskId
     * @param delegateUserId
     */
    @RequestMapping("task/delegate/{taskId}")
    @ResponseBody
    public String delegate(@PathVariable("taskId") String taskId, @RequestParam("delegateUserId") String delegateUserId) {
        taskService.delegateTask(taskId, delegateUserId);
        return "success";
    }

}
