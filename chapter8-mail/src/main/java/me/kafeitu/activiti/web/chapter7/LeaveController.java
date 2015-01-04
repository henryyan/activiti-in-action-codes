package me.kafeitu.activiti.web.chapter7;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import me.kafeitu.activiti.chapter7.entity.Leave;
import me.kafeitu.activiti.chapter7.service.LeaveManager;
import me.kafeitu.activiti.chapter7.service.LeaveWorkflowService;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假控制器
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter7/leave")
public class LeaveController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LeaveManager leaveManager;

    @Autowired
    private LeaveWorkflowService leaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @RequestMapping(value = {"apply", ""})
    public String createForm(Model model) {
        model.addAttribute("leave", new Leave());
        return "/chapter7/leave/leave-apply";
    }

    /**
     * 启动请假流程
     */
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public String startWorkflow(Leave leave, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User user = UserUtil.getUserFromSession(session);
            Map<String, Object> variables = new HashMap<String, Object>();
            ProcessInstance processInstance = leaveService.startWorkflow(leave, user.getId(), variables);
            redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstance.getId());
        } catch (ActivitiException e) {
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.warn("没有部署流程!", e);
                redirectAttributes.addFlashAttribute("error", "没有部署请假流程");
            } else {
                logger.error("启动请假流程失败：", e);
                redirectAttributes.addFlashAttribute("error", "系统内部错误！");
            }
        } catch (Exception e) {
            logger.error("启动请假流程失败：", e);
            redirectAttributes.addFlashAttribute("error", "系统内部错误！");
        }
        return "redirect:/chapter7/leave/apply";
    }

    /**
     * 任务列表
     *
     * @param leave
     */
    @RequestMapping(value = "task/list")
    public ModelAndView taskList(HttpSession session) {
        ModelAndView mav = new ModelAndView("/chapter7/leave/leave-task-list");
        String userId = UserUtil.getUserFromSession(session).getId();
        List<Leave> results = leaveService.findTodoTasks(userId);
        mav.addObject("records", results);
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
        return "redirect:/chapter7/leave/task/list";
    }

    /**
     * 任务列表
     *
     * @param leave
     */
    @RequestMapping(value = "task/view/{taskId}")
    public ModelAndView showTaskView(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Leave leave = leaveManager.get(new Long(processInstance.getBusinessKey()));
        ModelAndView mav = new ModelAndView("/chapter7/leave/task-" + task.getTaskDefinitionKey());
        mav.addObject("leave", leave);
        mav.addObject("task", task);
        return mav;
    }

    /**
     * 完成任务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "task/complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    @SuppressWarnings("unchecked")
    public String complete(@PathVariable("id") String taskId, @RequestParam(value = "saveEntity", required = false) String saveEntity,
                           @ModelAttribute("preloadLeave") Leave leave, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        boolean saveEntityBoolean = BooleanUtils.toBoolean(saveEntity);
        Map<String, Object> variables = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        try {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                if (parameterName.startsWith("p_")) {
                    // 参数结构：p_B_name，p为参数的前缀，B为类型，name为属性名称
                    String[] parameter = parameterName.split("_");
                    if (parameter.length == 3) {
                        String paramValue = request.getParameter(parameterName);
                        Object value = paramValue;
                        if (parameter[1].equals("B")) {
                            value = BooleanUtils.toBoolean(paramValue);
                        } else if (parameter[1].equals("DT")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            value = sdf.parse(paramValue);
                        }
                        variables.put(parameter[2], value);
                    } else {
                        throw new RuntimeException("invalid parameter for activiti variable: " + parameterName);
                    }
                }
            }
            leaveService.complete(leave, saveEntityBoolean, taskId, variables);
            redirectAttributes.addFlashAttribute("message", "任务已完成");
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            request.setAttribute("error", "完成任务失败");
        }
        return "redirect:/chapter7/leave/task/list";
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadLeave")
    public Leave getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return leaveManager.get(id);
        }
        return new Leave();
    }

}
