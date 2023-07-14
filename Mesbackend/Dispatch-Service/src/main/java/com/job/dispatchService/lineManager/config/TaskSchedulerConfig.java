package com.job.dispatchService.lineManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-16:21
 * @description
 */
@Configuration
//该注解可以启动TaskScheduling,实现SchedulingConfigurer是为了对你的定时任务有一些个性化的设置
@EnableScheduling
public class TaskSchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //实现定时任务必须要有线程池,这里即传入一个线程池,是一个自定义方法,在下面实现
        taskRegistrar.setScheduler(schedulerThreadPool());
    }


    /**
     * 定时任务中的线程池
     * @return
     */
    //里面标注方法的意思就是在这个bean destroy的时候,调用ScheduledThreadPoolExecutor类的shutdown方法优雅的关闭线程池
    @Bean(destroyMethod = "shutdown")
    public ScheduledThreadPoolExecutor schedulerThreadPool() {
               /*ScheduledThreadPoolExecutor这个类是实现了定时任务的线程池
               Runtime类解析,每一个java运行程序都有一个Runtime类实例,使当前运行程序能够与运行环境相关联,getRuntime方法返回当前
                运行程序的Runtime对象,avaliableProcessors方法返回可用处理器的数目,用返回的处理器的数目充当corePoolSize
                */
        return new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("当前任务执行失败"+r);
                    }
                });

    }



}
