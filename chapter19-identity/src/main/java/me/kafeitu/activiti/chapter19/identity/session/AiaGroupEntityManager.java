package me.kafeitu.activiti.chapter19.identity.session;

import me.kafeitu.activiti.chapter19.identity.AiaRoleManager;
import me.kafeitu.activiti.chapter19.identity.entity.AiaRole;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Henry Yan
 */
public class AiaGroupEntityManager extends GroupEntityManager {

    private AiaRoleManager aiaRoleManager;

    public void setAiaRoleManager(AiaRoleManager aiaRoleManager) {
        this.aiaRoleManager = aiaRoleManager;
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<AiaRole> roles = aiaRoleManager.findByUserId(new Long(userId));
        List<Group> groups = new ArrayList<Group>(roles.size());
        for (AiaRole aiaRole : roles) {
            GroupEntity group = new GroupEntity();
            group.setName(aiaRole.getRoleName()); // 角色中文名称
            group.setId(aiaRole.getRoleCode()); // 角色英文名称
            groups.add(group);
        }
        return groups;
    }
}
