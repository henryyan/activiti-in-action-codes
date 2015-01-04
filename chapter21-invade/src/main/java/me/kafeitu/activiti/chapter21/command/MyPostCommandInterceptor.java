package me.kafeitu.activiti.chapter21.command;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;

/**
 * 自定义的Post类型命令拦截器
 * @author: Henry Yan
 */
public class MyPostCommandInterceptor extends AbstractCommandInterceptor {

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {

        System.out.println("后置命令拦截器-> 拦截了命令：" + command);

        return next.execute(config, command);
    }
}