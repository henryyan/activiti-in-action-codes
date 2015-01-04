package me.kafeitu.activiti.chapter21.bpmn;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.parse.BpmnParseHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Henry Yan
 */
public class MyPostParseHandler implements BpmnParseHandler {

    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        Set<Class< ? extends BaseElement>> types = new HashSet<Class<? extends BaseElement>>();
        types.add(Process.class);
        types.add(UserTask.class);
        return types;
    }

    @Override
    public void parse(BpmnParse bpmnParse, BaseElement element) {
        if (element instanceof Process) {
            ProcessDefinitionEntity processDefinition = bpmnParse.getCurrentProcessDefinition();
            String key = processDefinition.getKey();
            processDefinition.setKey(key + "-modified-by-post-parse-handler");
        } else if (element instanceof UserTask) {
            UserTask userTask = (UserTask) element;
            List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
            System.out.println("UserTask:[" + userTask.getName() + "]的输出流：");
            for (SequenceFlow outgoingFlow : outgoingFlows) {
                System.out.println("\t" + outgoingFlow.getTargetRef());
            }
            System.out.println();
        }
    }
}
