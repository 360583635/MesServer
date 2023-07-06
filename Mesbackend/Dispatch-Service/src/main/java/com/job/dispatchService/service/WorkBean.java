package com.job.dispatchService.service;

import com.job.dispatchService.pojo.Work;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WorkBean {
    @Bean
    public static Work getWork(){
        return new Work();
    }
}
