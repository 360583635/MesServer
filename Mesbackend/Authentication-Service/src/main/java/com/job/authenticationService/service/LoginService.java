package com.job.authenticationService.service;

import com.job.authenticationService.pojo.ResponseResult;
import com.job.common.pojo.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


public interface LoginService{
    ResponseResult login(Users users);

    ResponseResult logout();
}

