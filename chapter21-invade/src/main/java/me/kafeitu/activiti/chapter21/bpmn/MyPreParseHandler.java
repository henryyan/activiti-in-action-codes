package me.kafeitu.activiti.chapter21.bpmn;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;

/**
 * @author: Henry Yan
 */
public class MyPreParseHandler extends AbstractBpmnParseHandler<Process> {

    protected Class< ? extends BaseElement> getHandledType() {
        return Process.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, Process element) {
        element.setName(element.getName() + "-被PRE解析器修改");
    }
}
