package me.kafeitu.activiti.chapter19.identity;

import me.kafeitu.activiti.chapter19.identity.entity.AiaDepartment;

/**
 * 部门实体管理
 * @author: Henry Yan
 */
public interface AiaDepartmentManager {

    // 根据ID查询部门
    AiaDepartment get(Long id);

    // 保存部门
    AiaDepartment save(AiaDepartment user);

    // 删除部门
    void delete(Long id);

}
