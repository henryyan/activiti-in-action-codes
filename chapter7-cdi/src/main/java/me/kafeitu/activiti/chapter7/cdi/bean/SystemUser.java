package me.kafeitu.activiti.chapter7.cdi.bean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author: Henry Yan
 */
@Named
@SessionScoped
public class SystemUser implements Serializable {

    private String userId = "kafeitu";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
