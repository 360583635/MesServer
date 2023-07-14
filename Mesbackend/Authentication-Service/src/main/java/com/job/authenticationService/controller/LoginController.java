package com.job.authenticationService.controller;

import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.service.LoginService;
import com.job.common.pojo.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/authen")
public class LoginController {


    @Autowired
    private LoginService loginService;
    //用户登录
    @PostMapping ("/login")
    public ResponseResult  login (@RequestBody Users users)  {
        //System.out.println("10001");
        System.out.println(users);
        return loginService.login(users);
    }
    //退出登录
    @RequestMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();

    }




}

