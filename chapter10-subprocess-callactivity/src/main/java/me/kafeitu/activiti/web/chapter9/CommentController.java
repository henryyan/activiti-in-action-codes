package me.kafeitu.activiti.web.chapter9;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    public Boolean addComment(@RequestParam("taskId") String taskId, @RequestParam("processInstanceId") String processInstanceId,
                              @RequestParam("message") String message, HttpSession session) {
        identityService.setAuthenticatedUserId(UserUtil.getUserFromSession(session).getId());
        taskService.addComment(taskId, processInstanceId, message);
        return true;
    }

    /**
     * 读取意见
     */
    @RequestMapping(value = "list/{processInstanceId}")
    @ResponseBody
    public Map<String, Object> list(@PathVariable("processInstanceId") String processInstanceId) {

        Map<String, Object> result = new HashMap<String, Object>();

        List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);

        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
        Map<String, String> taskNames = new HashMap<String, String>();
        for (HistoricTaskInstance historicTaskInstance : list) {
            taskNames.put(historicTaskInstance.getId(), historicTaskInstance.getName());
        }

        result.put("comments", taskComments);
        result.put("taskNames", taskNames);

        return result;
    }

}
