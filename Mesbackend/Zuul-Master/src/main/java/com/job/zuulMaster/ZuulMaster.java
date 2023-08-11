package com.job.zuulMaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author 菜狗
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.job.zuulMaster.mapper")
public class    ZuulMaster {
    public static void main(String[] args) {
        SpringApplication.run(ZuulMaster.class,args);

    }
}
