package me.kafeitu.activiti.chapter21.parse;

import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * @author: Henry Yan
 */
public class CustomServiceTaskBpmnParseHandler extends ServiceTaskParseHandler {

    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        super.executeParse(bpmnParse, serviceTask);

        ActivityImpl activity = findActivity(bpmnParse, serviceTask.getId());
        activity.setAsync(true);
    }
}