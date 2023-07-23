package com.job.dispatchService;

import com.job.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author 菜狗
 */
@SpringBootApplication(scanBasePackages = "com.job.dispatchService")
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@ComponentScan({"com.job.common","com.job.dispatchService"})
@EnableFeignClients(basePackages = "com.job.feign.clients",defaultConfiguration = DefaultFeignConfiguration.class)
@MapperScan("com.job.dispatchService.**.mapper*")
public class DispatchService {
    public static void main(String[] args) {
        SpringApplication.run(DispatchService.class,args);
    }
}
