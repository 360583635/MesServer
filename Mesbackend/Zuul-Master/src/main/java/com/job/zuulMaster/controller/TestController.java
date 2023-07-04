package com.job.zuulMaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.zuulMaster.pojo.User;
import com.job.zuulMaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 菜狗
 */
@RestController
@RequestMapping("zuul")
public class TestController {
    @Autowired
    private UserService userService;
    @RequestMapping("/get")
    public User get(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,"aa");
        User one = userService.getOne(queryWrapper);
        System.out.println(one);
        return one;
    }
}
