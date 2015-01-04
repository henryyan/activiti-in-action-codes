package me.kafeitu.activiti.chapter18.esb.camel.standlone;

import java.io.Serializable;

/**
 * @author: Henry Yan
 */
public class CamelLeaveBean implements Serializable {

    private Boolean result = false;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

}
