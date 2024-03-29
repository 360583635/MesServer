package com.job.authenticationService.controller;


import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.service.LoginService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.authenticationService.utils.RedisCache;
import com.job.common.pojo.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/authen")
//@CrossOrigin
public class LoginController {


    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 用户登录
     * @param users
     * @return
     */
    @PostMapping ("/login")
    public ResponseResult  login (@RequestBody Users users)  {;
        System.out.println(users);
        return loginService.login(users);
    }


    /*@RequestMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }*/

}

