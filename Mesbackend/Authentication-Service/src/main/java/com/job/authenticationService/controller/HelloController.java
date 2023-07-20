package com.job.authenticationService.Controller;

import com.job.authenticationService.pojo.LoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
