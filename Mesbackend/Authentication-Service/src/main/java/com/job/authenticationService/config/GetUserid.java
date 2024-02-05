package com.job.authenticationService.config;

import com.job.authenticationService.pojo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class GetUserid {
    public static String  getID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String  userid = loginUser.getUser().getId();
        log.info("获取用户Id:"+userid+"---com.job.authenticationService.config.GetUserid");
        return userid;
    }
}
