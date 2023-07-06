package com.job.dispatchService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author 菜狗
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DispatchService {
    public static void main(String[] args) {
        SpringApplication.run(DispatchService.class,args);
    }
}
