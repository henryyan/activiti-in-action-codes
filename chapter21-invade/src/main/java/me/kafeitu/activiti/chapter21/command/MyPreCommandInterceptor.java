package me.kafeitu.activiti.chapter21.command;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;

/**
 * 自定义的Pre类型命令拦截器
 * @author: Henry Yan
 */
public class MyPreCommandInterceptor extends AbstractCommandInterceptor {

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {

        System.out.println("前置命令拦截器-> 拦截了命令：" + command);

        return next.execute(config, command);
    }
}