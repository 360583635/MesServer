package com.job.productionManagementService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @Author 猫
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com/job/productionManagementService/mapper")
public class ProductionManagementServer {
    public static void main(String[] args) {
        SpringApplication.run(ProductionManagementServer.class,args);
    }
}
