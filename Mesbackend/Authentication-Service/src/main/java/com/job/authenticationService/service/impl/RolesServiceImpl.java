package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.service.RolesService;
import com.job.common.pojo.Roles;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper,Roles> implements RolesService{
    @Override
    public void customMethod() {
        // 自定义方法的具体实现
    }
    }

