package me.kafeitu.activiti.chapter21.parse;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * @author: Henry Yan
 */
public class CustomUserTaskBpmnParseHandler extends UserTaskParseHandler {

    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);

        ActivityImpl activity = findActivity(bpmnParse, userTask.getId());
        activity.setAsync(true);
    }
}
