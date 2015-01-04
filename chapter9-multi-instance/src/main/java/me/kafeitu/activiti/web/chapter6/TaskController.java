package me.kafeitu.activiti.web.chapter6;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Map.Entry;

/**
 * 任务控制器
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter6")
public class TaskController /*extends AbstractController*/ {

    private static String TASK_LIST = "redirect:/chapter6/task/list";

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

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

        // 等待签收的任务
        List<Task> waitingClaimTasks = taskService.createTaskQuery().taskCandidateUser(user.getId()).list();

        // 合并两种任务
        List<Task> allTasks = new ArrayList<Task>();
        allTasks.addAll(doingTasks);
        allTasks.addAll(waitingClaimTasks);*/

        // 5.16版本可以使用一下代码待办查询
        List<Task> allTasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();

        mav.addObject("tasks", allTasks);
        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = UserUtil.getUserFromSession(session).getId();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
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
        if (taskFormData.getFormKey() != null) {
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("task", task);
            mav.addObject("taskFormData", renderedTaskForm);
            mav.addObject("hasFormKey", true);
        } else {
            mav.addObject("taskFormData", taskFormData);
        }
        return mav;
    }

    /**
     * 读取启动流程的表单字段
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "task/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
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

}
