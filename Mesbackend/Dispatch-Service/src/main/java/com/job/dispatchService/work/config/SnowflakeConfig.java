package com.job.dispatchService.work.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import wiki.xsx.core.snowflake.config.Snowflake;

@Configuration
@Component
public class SnowflakeConfig {
    @Value("${application.data-center-id}")
    private Long datacenterId;
    @Value("${application.worker-id}")
    private Long workerId;
    @Bean
    public Snowflake snowflake(){
        return new Snowflake(workerId, datacenterId);
    }
}
