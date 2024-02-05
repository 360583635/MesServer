package com.job.authenticationService.service.impl;

import com.job.authenticationService.pojo.LoginUser;
import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.service.LoginService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.authenticationService.utils.RedisCache;
import com.job.common.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class  LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;


    /**
     * 用户登录
     * @param users
     * @return
     */
    @Override
    public ResponseResult login(Users users) {
        log.info("登录服务开始");
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getName(), users.getPassword());
        log.info("进行用户认证："+authenticationToken);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        log.info("用户认证结束");

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            System.out.println("认证失败");
            throw new RuntimeException("登录失败");
        }

        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loignUser = (LoginUser) authenticate.getPrincipal();
        System.out.println(loignUser);
        String userid = loignUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        System.out.println(map);

        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("login" + userid, loignUser);
        //把token响应给前端
        System.out.println("success");
        return new ResponseResult(200, "登录成功",map);
    }

//    @Override
//    public ResponseResult logout() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        String  userid = loginUser.getUser().getId();
//        redisCache.deleteObject("login"+userid);
//        return new ResponseResult(200,"退出成功");
//    }

}

