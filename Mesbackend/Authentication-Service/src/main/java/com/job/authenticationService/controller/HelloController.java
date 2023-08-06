package com.job.authenticationService.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    //@PreAuthorize("hasAuthority('login')")
    public String hello(){
        System.out.println("hhhh");
        return "hello";
    }
}
