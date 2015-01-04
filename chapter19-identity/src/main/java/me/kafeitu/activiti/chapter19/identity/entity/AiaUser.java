package me.kafeitu.activiti.chapter19.identity.entity;

/**
 * 用户对象
 * @author: Henry Yan
 */
public class AiaUser {

    private Long id;
    private String loginName;
    private String password;
    private String userName;
    private AiaDepartment dept;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AiaDepartment getDept() {
        return dept;
    }

    public void setDept(AiaDepartment dept) {
        this.dept = dept;
    }
}
