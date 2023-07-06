package com.job.authenticationService.service.impl;

import com.job.authenticationService.pojo.LoginUser;
import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.pojo.Users;
import com.job.authenticationService.service.LoginService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.authenticationService.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;



    @Override
    public ResponseResult login(Users users) {
        System.out.println("12345");
        //AuthenticationManager authenticate进行用户认真
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getName(), users.getPassword());
        System.out.println("5678");
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
       // Authentication authenticate=authenticationManager.authenticate(authenticationToken);
        System.out.printf("111");

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }


        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loignUser = (LoginUser) authenticate.getPrincipal();
        String userid = loignUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        System.out.println(map);

        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("login" + userid, loignUser);
        //把token响应给前端


        return new ResponseResult(200, "登录成功", map);
    }

}
