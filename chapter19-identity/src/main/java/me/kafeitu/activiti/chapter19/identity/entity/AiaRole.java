package me.kafeitu.activiti.chapter19.identity.entity;

import java.util.List;

/**
 * 角色实体
 * @author: Henry Yan
 */
public class AiaRole {

    private Long id;
    private String roleName;
    private String roleCode;
    private List<AiaResource> resources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<AiaResource> getResources() {
        return resources;
    }

    public void setResources(List<AiaResource> resources) {
        this.resources = resources;
    }
}
