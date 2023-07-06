package com.job.authenticationService.Controller;

import com.alibaba.nacos.shaded.com.google.j2objc.annotations.ReflectionSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
