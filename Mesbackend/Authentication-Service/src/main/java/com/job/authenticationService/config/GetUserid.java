package com.job.authenticationService.config;

import com.job.authenticationService.pojo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class GetUserid {
    public static String  getID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String  userid = loginUser.getUser().getId();
        System.out.println(userid);
        return userid;
    }
}
