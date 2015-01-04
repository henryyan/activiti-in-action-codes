package me.kafeitu.activiti.chapter6.util;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @author henryyan
 */
public class InitEngineeDatabase {

    public static void main(String[] args) {
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
    }

}
