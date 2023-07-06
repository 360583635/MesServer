package com.job.dispatchService.service;

import com.job.dispatchService.pojo.TWork;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WorkBean {
    @Bean
    public static TWork getBean(){
        TWork work = new TWork();
        return work;
    }
}
