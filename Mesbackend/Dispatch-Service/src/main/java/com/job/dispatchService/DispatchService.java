package com.job.dispatchService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author 菜狗
 */
@SpringBootApplication(scanBasePackages = "com.job.dispatchService")
@EnableDiscoveryClient
//@EnableFeignClients
@MapperScan("com.job.dispatchService.**.mapper*")
public class DispatchService {
    public static void main(String[] args) {
        SpringApplication.run(DispatchService.class,args);
    }
}
