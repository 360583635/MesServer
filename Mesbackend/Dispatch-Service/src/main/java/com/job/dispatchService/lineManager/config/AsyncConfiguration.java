package com.job.dispatchService.lineManager.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-20:58
 * @description
 */
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 异步任务使用的默认线程池
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核销线程数
        taskExecutor.setCorePoolSize(15);
        //线程池维护线程的最大数量,只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(25);
        //缓存队列
        taskExecutor.setQueueCapacity(200);
        //允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(60);
        //异步方法内部线程名称
        taskExecutor.setThreadNamePrefix("async-");
        taskExecutor.setAwaitTerminationSeconds(60);
        //使用自定义拒绝策略,或者自带的几种拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    //指定默认线程池
    public Executor getAsyncExecutor() {
        return executor();
    }

    //线程异常处理
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (ex , method , params) ->
                LOGGER.error("线程池执行任务发送未知错误,执行方法：{}",method.getName(),ex);
    }
}
