package com.job.authenticationService.config;

import com.job.authenticationService.utils.JwtUtil;
import com.job.authenticationService.utils.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 庸俗可耐
 * @create 2024-02-05-15:47
 * @description
 */
@Component
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        try {
            blacklist(token);
        } catch (Exception e) {
            throw new RuntimeException("加入黑名单失败");
        }
        SecurityContextHolder.clearContext();
    }

    /**
     * 加入黑名单
     *
     * @param token
     * @throws Exception
     */
    private void blacklist(String token) throws Exception {
        String jti = JwtUtil.getJti(token);
        Long expires = JwtUtil.getExpires(token);
        redisCache.setCacheObject("token:black:"+jti+":string", StringUtils.EMPTY, expires, TimeUnit.SECONDS);
    }
}
