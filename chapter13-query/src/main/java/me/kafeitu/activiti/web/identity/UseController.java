package me.kafeitu.activiti.web.identity;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关控制器
 *
 * @author HenryYan
 */
@Controller
@RequestMapping("/user")
public class UseController {

    private static Logger logger = LoggerFactory.getLogger(UseController.class);

    // Activiti Identify Service
    @Autowired
    private IdentityService identityService;

    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/logon")
    public String logon(@RequestParam("username") String userName, @RequestParam("password") String password, HttpSession session) {
        logger.debug("logon request: {username={}, password={}}", userName, password);
        boolean checkPassword = identityService.checkPassword(userName, password);
        if (checkPassword) {

            // 查看用户是否存在
            User user = identityService.createUserQuery().userId(userName).singleResult();
            UserUtil.saveUserToSession(session, user);

      /*
       * 读取角色
       */
            List<Group> groupList = identityService.createGroupQuery().groupMember(user.getId()).list();
            session.setAttribute("groups", groupList);

            String[] groupNames = new String[groupList.size()];
            for (int i = 0; i < groupNames.length; i++) {
                groupNames[i] = groupList.get(i).getName();
            }
            session.setAttribute("groupNames", ArrayUtils.toString(groupNames));

            return "redirect:/main/index";
        } else {
            return "redirect:/login.jsp?error=true";
        }
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, List<User>> usersByGroup() {
        List<Group> groups = identityService.createGroupQuery().list();
        Map<String, List<User>> usersByGroup = new HashMap<String, List<User>>(groups.size());
        for (Group group : groups) {
            List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
            usersByGroup.put(group.getName(), users);
        }
        return usersByGroup;
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/login";
    }

}
