package com.job.dispatchService.work.service;

import com.job.common.pojo.Work;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WorkBean {
    @Bean
    public static Work getWork(){
        return new Work();
    }
}
