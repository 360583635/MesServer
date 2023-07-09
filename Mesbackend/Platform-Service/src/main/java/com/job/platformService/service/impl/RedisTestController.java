//package com.job.platformService.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//import com.job.platformService.result.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Objects;
//
//@RestController
//
//
//    @RequestMapping("redis")
//    public class RedisTestController {
//        @Autowired
//        RedisTemplate redisTemplate;
//
//        @GetMapping("getValue")
//        public Object getValue() {
//            redisTemplate.opsForValue().set("zyx1","testValue");
//            System.out.println(111);
//            Object o = redisTemplate.opsForValue().get("user:222");
//            return o;
//
//
//        }
//    }
//
//
//
//
//
//
