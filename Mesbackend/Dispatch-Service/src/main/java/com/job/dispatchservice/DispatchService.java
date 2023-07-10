package com.job.dispatchservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author 菜狗
 */
@SpringBootApplication(scanBasePackages = "com.job.dispatchservice")
@EnableDiscoveryClient
@EnableAsync(proxyTargetClass = true)
//@EnableFeignClients
@MapperScan("com.job.dispatchService.**.mapper*")
public class DispatchService {
    public static void main(String[] args) {
        SpringApplication.run(DispatchService.class,args);
    }
}
