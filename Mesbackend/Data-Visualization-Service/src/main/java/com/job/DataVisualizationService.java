package com.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author 菜狗
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DataVisualizationService {
    public static void main(String[] args) {
        SpringApplication.run(DataVisualizationService.class,args);
    }
}
