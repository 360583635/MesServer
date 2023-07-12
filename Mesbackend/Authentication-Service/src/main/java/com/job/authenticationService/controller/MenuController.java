package com.job.authenticationService.controller;

import com.job.authenticationService.pojo.ResponseResult;
import com.job.common.pojo.Menus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authen/menus")
public class MenuController {
    //1、添加权限
    @RequestMapping("/add")
    public ResponseResult  addMenus(){
        System.out.println("menusbinggou");
        return null;
    }
    //2、删除权限
    @RequestMapping("/delete")
    public ResponseResult deleteMenus(){
        return null;
    }
    //3、修改权限
    @RequestMapping("/update")
    public ResponseResult updateMenus(){
        return null;
    }

    //4、
}
