package com.job.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author 庸俗可耐
 * @create 2023-04-24-20:29
 * @description
 */

public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }
}
