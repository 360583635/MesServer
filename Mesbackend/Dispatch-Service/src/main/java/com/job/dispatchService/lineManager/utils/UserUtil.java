package com.job.dispatchService.lineManager.utils;

import com.job.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class UserUtil {


    public static String getUserId(HttpServletRequest request){
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
             return userId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

    }


}
