package me.kafeitu.activiti.web.chapter9;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter9/comment")
public class CommentController {

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    @Autowired
    HistoryService historyService;

    /**
     * 保存意见
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Boolean addComment(@RequestParam("taskId") String taskId, @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                              @RequestParam("message") String message, HttpSession session) {
        identityService.setAuthenticatedUserId(UserUtil.getUserFromSession(session).getId());
        taskService.addComment(taskId, processInstanceId, message);
        return true;
    }

    /**
     * 读取意见
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                    @RequestParam(value = "taskId", required = false) String taskId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> commentAndEventsMap = new HashMap<String, Object>();

    /*
     * 根据不同情况使用不同方式查询
     */
        if (StringUtils.isNotBlank(processInstanceId)) {
            List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
            for (Comment comment : processInstanceComments) {
                String commentId = (String) PropertyUtils.getProperty(comment, "id");
                commentAndEventsMap.put(commentId, comment);
            }

            // 提取任务任务名称
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
            Map<String, String> taskNames = new HashMap<String, String>();
            for (HistoricTaskInstance historicTaskInstance : list) {
                taskNames.put(historicTaskInstance.getId(), historicTaskInstance.getName());
            }
            result.put("taskNames", taskNames);

        }

    /*
     * 查询所有类型的事件
     */
        if (StringUtils.isNotBlank(taskId)) { // 根据任务ID查询
            List<Event> taskEvents = taskService.getTaskEvents(taskId);
            for (Event event : taskEvents) {
                String eventId = (String) PropertyUtils.getProperty(event, "id");
                commentAndEventsMap.put(eventId, event);
            }
        }

        result.put("events", commentAndEventsMap.values());

        return result;
    }

}
