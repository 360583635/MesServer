package com.job.authenticationService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author 菜狗
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.job.authenticationService.mapper")
public class AuthenticationService {


    public static void main(String[] args) {
        SpringApplication.run(AuthenticationService.class,args);

    }


}
