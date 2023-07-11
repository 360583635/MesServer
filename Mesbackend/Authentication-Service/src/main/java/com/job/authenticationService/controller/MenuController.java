package com.job.authenticationService.controller;

import com.job.common.pojo.Menus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authen/menus")
public class MenuController {
    @RequestMapping("/add")
    public String  addMenus(Menus menus){

        System.out.println("menusbinggou");
        return "menus!";
    }
}
