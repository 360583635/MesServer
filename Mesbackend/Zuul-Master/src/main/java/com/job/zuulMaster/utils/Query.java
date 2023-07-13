package com.job.zuulMaster.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Users;
import com.job.zuulMaster.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Query {
    @Autowired
    private UsersService usersService;
    private Integer BLACKSTATE=0;
    public List<String > query(){
        LambdaQueryWrapper<Users> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Users::getIsBlack,BLACKSTATE);
        List<Users> list=usersService.list(wrapper);
        List<String > idlist=new ArrayList<>();
        for (Users users : list) {
            idlist.add(users.getId());
        }
        return idlist;

    }
}
