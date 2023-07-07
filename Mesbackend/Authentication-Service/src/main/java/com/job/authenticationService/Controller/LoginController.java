package com.job.authenticationService.Controller;

import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.pojo.Users;
import com.job.authenticationService.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authen")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping ("/login")
    public ResponseResult login( Users users){
        //System.out.println("10001");
        System.out.println(users);
        return loginService.login(users);

    }


}

