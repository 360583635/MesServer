package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.service.RolesService;
import com.job.authenticationService.service.UsersService;
import com.job.common.pojo.Users;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
