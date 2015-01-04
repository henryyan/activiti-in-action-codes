package me.kafeitu.activiti.web.chapter14;

import me.kafeitu.activiti.chapter14.ProcessDefinitionService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程定义控制器
 * User: henryyan
 */
@Controller
@RequestMapping(value = "/chapter14/process")
public class ProcessManagerController {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcessDefinitionService processDefinitionService;

    /**
     * 流程定义状态控制
     *
     * @param state               active|suspend
     * @param processDefinitionId 流程定义ID
     * @return
     */
    @RequestMapping(value = "{state}", method = RequestMethod.POST)
    public String changeState(@PathVariable(value = "state") String state,
                              @RequestParam(value = "processDefinitionId") String processDefinitionId,
                              @RequestParam(value = "cascade", required = false) boolean cascadeProcessInstances,
                              @RequestParam(value = "effectiveDate", required = false) String strEffectiveDate) {

        Date effectiveDate = null;

        if (StringUtils.isNotBlank(strEffectiveDate)) {
            try {
                effectiveDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(strEffectiveDate);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }

        if (StringUtils.equals("active", state)) {
            repositoryService.activateProcessDefinitionById(processDefinitionId, cascadeProcessInstances, effectiveDate);
        } else if (StringUtils.equals("suspend", state)) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, cascadeProcessInstances, effectiveDate);
        }
        return "redirect:/chapter5/process-list";
    }

    /**
     * 设置流程定义对象的候选人、候选组
     * @return
     */
    @RequestMapping(value = "startable/set/{processDefinitionId}", method = RequestMethod.POST)
    @ResponseBody
    public String addStartables(@PathVariable("processDefinitionId") String processDefinitionId,
            @RequestParam(value = "users[]", required = false) String[] users, @RequestParam(value = "groups[]", required = false) String[] groups) {
        processDefinitionService.setStartables(processDefinitionId, users, groups);
        return "true";
    }

    /**
     * 读取已设置的候选启动人、组
     * @param processDefinitionId
     * @return
     */
    @RequestMapping(value = "startable/read/{processDefinitionId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<String>> readStartableData(@PathVariable("processDefinitionId") String processDefinitionId) {
        Map<String, List<String>> datas = new HashMap<String, List<String>>();
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<String> groups = new ArrayList<String>();

        List<IdentityLink> links = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
        for (IdentityLink link : links) {
            if (StringUtils.isNotBlank(link.getUserId())) {
                users.add(link.getUserId());
            }
            if (StringUtils.isNotBlank(link.getGroupId())) {
                groups.add(link.getGroupId());
            }
        }
        datas.put("users", users);
        datas.put("groups", groups);
        return datas;
    }

}
