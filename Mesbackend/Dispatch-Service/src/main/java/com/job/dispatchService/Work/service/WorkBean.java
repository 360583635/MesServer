package com.job.dispatchService.Work.service;

import com.job.dispatchService.Work.pojo.Work;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WorkBean {
    @Bean
    public static Work getWork(){
        return new Work();
    }
}
