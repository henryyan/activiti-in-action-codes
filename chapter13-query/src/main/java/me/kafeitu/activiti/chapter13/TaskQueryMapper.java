package me.kafeitu.activiti.chapter13;

import me.kafeitu.activiti.web.chapter13.RunningTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

/**
 * 使用MyBatis语法的查询映射接口
 *
 * @author: Henry Yan
 */
public interface TaskQueryMapper {

    /**
     * 查询所有正在运行的任务
     *
     * @return
     */
    @Select("select art.ID_, art.NAME_, art.ASSIGNEE_, art.CREATE_TIME_," +
            " art.PROC_INST_ID_, art.PROC_DEF_ID_, arp.NAME_ PROCESS_NAME" +
            " from ACT_RU_TASK art" +
            " inner join ACT_RE_PROCDEF arp on art.PROC_DEF_ID_ = arp.ID_" +
            " where arp.SUSPENSION_STATE_ = '1'")
    @Results(value = {
            @Result(id = true, property = "id", column = "ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "name", column = "NAME_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "assignee", column = "ASSIGNEE_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "createTime", column = "CREATE_TIME_", javaType = Date.class, jdbcType = JdbcType.DATE),
            @Result(property = "processInstanceId", column = "PROC_INST_ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processDefinitionId", column = "PROC_DEF_ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processName", column = "PROCESS_NAME", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    List<RunningTask> selectRunningTasks();

    /**
     * 查询所有正在运行的任务，根据流程KEY过滤
     *
     * @param processKey
     * @return
     */
    @Select("select art.ID_, art.NAME_, art.ASSIGNEE_, art.CREATE_TIME_," +
            " art.PROC_INST_ID_, art.PROC_DEF_ID_, arp.NAME_ PROCESS_NAME" +
            " from ACT_RU_TASK art" +
            " inner join ACT_RE_PROCDEF arp on art.PROC_DEF_ID_ = arp.ID_" +
            " where arp.SUSPENSION_STATE_ = '1' and arp.KEY_ = #{processKey}")
    @Results(value = {
            @Result(id = true, property = "id", column = "ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "name", column = "NAME_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "assignee", column = "ASSIGNEE_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "createTime", column = "CREATE_TIME_", javaType = Date.class, jdbcType = JdbcType.DATE),
            @Result(property = "processInstanceId", column = "PROC_INST_ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processDefinitionId", column = "PROC_DEF_ID_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "processName", column = "PROCESS_NAME", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    List<RunningTask> selectRunningTasksByProcessKey(String processKey);

    @Select("select * from ACT_RU_TASK RES" +
            " inner join ACT_RU_VARIABLE VAR on VAR.PROC_INST_ID_ = RES.PROC_INST_ID_" +
            " where VAR.NAME_ = #{variableName}")
    List<TaskEntity> findTasks(String variableName);
}
