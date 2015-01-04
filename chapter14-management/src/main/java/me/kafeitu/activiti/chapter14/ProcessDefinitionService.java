package me.kafeitu.activiti.chapter14;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程定义Service
 * User: henryyan
 */
@Transactional
@Service
public class ProcessDefinitionService {

    @Autowired
    RepositoryService repositoryService;

    /**
     * 设置候选启动人、组
     * @param processDefinitionId
     * @param userArray
     * @param groupArray
     */
    public void setStartables(String processDefinitionId, String[] userArray, String[] groupArray) {

        // 1、清理现有的设置
        List<IdentityLink> links = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
        for (IdentityLink link : links) {
            if (StringUtils.isNotBlank(link.getUserId())) {
                repositoryService.deleteCandidateStarterUser(processDefinitionId, link.getUserId());
            }
            if (StringUtils.isNotBlank(link.getGroupId())) {
                repositoryService.deleteCandidateStarterGroup(processDefinitionId, link.getGroupId());
            }
        }

        // 2.1、循环添加候选人
        if (!ArrayUtils.isEmpty(userArray)) {
            for (String user : userArray) {
                repositoryService.addCandidateStarterUser(processDefinitionId, user);
            }
        }

        // 2.2、循环添加候选组
        if (!ArrayUtils.isEmpty(groupArray)) {
            for (String group : groupArray) {
                repositoryService.addCandidateStarterGroup(processDefinitionId, group);
            }
        }
    }
}
