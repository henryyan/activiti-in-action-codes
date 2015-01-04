package me.kafeitu.activiti.web.chapter13;

import me.kafeitu.activiti.chapter13.Page;
import me.kafeitu.activiti.chapter13.PageUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已归档流程实例
 * User: henryyan
 */
@Controller
@RequestMapping(value = "/chapter13/history/process")
public class HistoryProcessInstanceController {

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    /**
     * 查询已结束流程实例
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "finished/list")
    public ModelAndView finishedProcessInstanceList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("chapter13/finished-process");
        Page<HistoricProcessInstance> page = new Page<HistoricProcessInstance>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(pageParams[0], pageParams[1]);

        // 查询流程定义对象
        Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            definitionCache(definitionMap, historicProcessInstance.getProcessDefinitionId());
        }

        page.setResult(historicProcessInstances);
        page.setTotalCount(historicProcessInstanceQuery.count());
        mav.addObject("page", page);
        mav.addObject("definitions", definitionMap);

        return mav;
    }

    /**
     * 查询已结束流程实例
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "finished/manager")
    public ModelAndView finishedProcessInstanceListForManager(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("chapter13/finished-process-manager");
        Page<HistoricProcessInstance> page = new Page<HistoricProcessInstance>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(pageParams[0], pageParams[1]);

        // 查询流程定义对象
        Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            definitionCache(definitionMap, historicProcessInstance.getProcessDefinitionId());
        }

        page.setResult(historicProcessInstances);
        page.setTotalCount(historicProcessInstanceQuery.count());
        mav.addObject("page", page);
        mav.addObject("definitions", definitionMap);

        return mav;
    }

    /**
     * 查询历史相关信息
     *
     * @param processInstanceId
     * @return
     */
    @RequestMapping(value = "finished/view/{processInstanceId}")
    public ModelAndView historyDatas(@PathVariable("processInstanceId") String processInstanceId) {
        ModelAndView mav = new ModelAndView("chapter13/view-finished-process");

        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

        // 查询历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        // 查询流程有关的变量
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();

        List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();

        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

        mav.addObject("historicProcessInstance", historicProcessInstance);
        mav.addObject("variableInstances", variableInstances);
        mav.addObject("activities", activityInstances);
        mav.addObject("formProperties", formProperties);
        mav.addObject("processDefinition", processDefinition);

        return mav;
    }

    /**
     * 删除历史流程数据
     *
     * @param processInstanceId
     * @return
     */
    @RequestMapping(value = "finished/delete/{processInstanceId}")
    public String deleteProcessInstance(@PathVariable("processInstanceId") String processInstanceId,
                                        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "ID为" + processInstanceId + "的历史流程已删除！");
        historyService.deleteHistoricProcessInstance(processInstanceId);
        return "redirect:/chapter13/history/process/finished/manager";
    }

    /**
     * 流程定义对象缓存
     *
     * @param definitionMap
     * @param processDefinitionId
     */
    private void definitionCache(Map<String, ProcessDefinition> definitionMap, String processDefinitionId) {
        if (definitionMap.get(processDefinitionId) == null) {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            processDefinitionQuery.processDefinitionId(processDefinitionId);
            ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

            // 放入缓存
            definitionMap.put(processDefinitionId, processDefinition);
        }
    }

}
