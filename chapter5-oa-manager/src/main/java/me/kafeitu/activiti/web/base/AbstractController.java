package me.kafeitu.activiti.web.base;

import me.kafeitu.activiti.chapter5.util.ActivitiUtils;
import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象Controller，提供一些基础的方法、属性
 *
 * @author henryyan
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ProcessEngine processEngine = null;

    public AbstractController() {
        super();
        processEngine = ActivitiUtils.getProcessEngine();
    }

}
