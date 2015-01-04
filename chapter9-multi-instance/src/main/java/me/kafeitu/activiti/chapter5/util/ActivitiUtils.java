package me.kafeitu.activiti.chapter5.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * Activiti工具类
 *
 * @author henryyan
 */
public class ActivitiUtils {

    private static ProcessEngine processEngine;

    /**
     * 单例模式获取引擎对象
     */
    public static ProcessEngine getProcessEngine() {
        if (processEngine == null) {
      /*
       * 使用默认的配置文件名称（activiti.cfg.xml）创建引擎对象
       */
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        }
        return processEngine;
    }

}
