package com.job.platformService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author 菜狗
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.job.platformService.mapper")
public class PlatformService {
    public static void main(String[] args) {
        SpringApplication.run(PlatformService.class,args);
    }
}
