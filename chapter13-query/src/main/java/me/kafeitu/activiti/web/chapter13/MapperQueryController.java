package me.kafeitu.activiti.web.chapter13;

import me.kafeitu.activiti.chapter13.TaskQueryMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.cmd.CustomSqlExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 使用MyBatis语法查询数据
 *
 * @author: Henry Yan
 */
@Controller
@RequestMapping(value = "/chapter13/query")
public class MapperQueryController {

    @Autowired
    ManagementService managementService;

    @Autowired
    RepositoryService repositoryService;

    /**
     * 查询正在运行的任务
     *
     * @param processKey
     * @return
     */
    @RequestMapping(value = "task/running")
    public ModelAndView list(@RequestParam(value = "processKey", required = false) final String processKey) {
        ModelAndView mav = new ModelAndView("chapter13/running-tasks");

        CustomSqlExecution<TaskQueryMapper, List<RunningTask>> customSqlExecution =
                new AbstractCustomSqlExecution<TaskQueryMapper, List<RunningTask>>(TaskQueryMapper.class) {

                    public List<RunningTask> execute(TaskQueryMapper customMapper) {

                        // 使用内置实体对象查询
                        // List<TaskEntity> taskByVariable = customMapper.findTasks("applyUserId");

                        List<RunningTask> tasks;
                        if (StringUtils.isBlank(processKey)) {
                            tasks = customMapper.selectRunningTasks();
                        } else {
                            tasks = customMapper.selectRunningTasksByProcessKey(processKey);
                        }
                        return tasks;
                    }
                };

        List<RunningTask> tasks = managementService.executeCustomSql(customSqlExecution);
        mav.addObject("tasks", tasks);

        // 读取引擎中所有的流程定义（只查询最新版本，目的在于获取流程定义的KEY和NAME）
        List<ProcessDefinition> processes = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        mav.addObject("processes", processes);



        return mav;
    }

}
