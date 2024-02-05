package com.job.authenticationService.filter;
//
import com.job.authenticationService.pojo.LoginUser;
import com.job.authenticationService.utils.JwtUtil;
import com.job.authenticationService.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
        response.addHeader("Cache-Control", "no-cache='set-cookie'");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Access-Control-Allow-Headers", "Authorization");
        /*if (Arrays.stream(JwtUtil.getWhiteList()).anyMatch(uri -> uri.equals(request.getServletPath()))) {
            filterChain.doFilter(request, response);
            return;
        }*/
        //获取token
        String token = request.getHeader("Authorization");
        String token1 = request.getParameter("Authorization");
        String jti = null;
        try {
            jti = JwtUtil.getJti(token);
        } catch (Exception e) {
            throw new RuntimeException("jti获取失败");
        }
        if(redisCache.redisTemplate.hasKey("token:black:"+ jti +":string")){
            throw new RuntimeException("用户未登录");
        }
        System.out.println("filtertoken:"+token);
        System.out.println("filtertoken1:"+token1);
        if (!StringUtils.hasText(token1)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token1);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
