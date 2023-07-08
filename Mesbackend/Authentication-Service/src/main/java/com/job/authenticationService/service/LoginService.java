package com.job.authenticationService.service;

import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.pojo.Users;

public interface LoginService{
    ResponseResult login(Users users);

    ResponseResult logout();
}

