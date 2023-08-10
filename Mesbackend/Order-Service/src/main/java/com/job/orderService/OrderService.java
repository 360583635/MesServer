package com.job.orderService;

import com.job.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author 菜狗
 */
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.job.common","com.job.orderService"})
@EnableFeignClients(basePackages = "com.job.feign.clients",defaultConfiguration = DefaultFeignConfiguration.class)
public class OrderService {
    public static void main(String[] args) {
        SpringApplication.run(OrderService.class,args);
    }
}
