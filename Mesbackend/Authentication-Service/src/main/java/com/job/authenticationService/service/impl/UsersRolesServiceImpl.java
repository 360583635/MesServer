package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.mapper.UsersRolesMapper;
import com.job.authenticationService.service.UsersRolesService;
import com.job.authenticationService.service.UsersService;
import com.job.common.pojo.Users;
import com.job.common.pojo.UsersRoles;
import org.springframework.stereotype.Service;

@Service
public class UsersRolesServiceImpl extends ServiceImpl<UsersRolesMapper, UsersRoles> implements UsersRolesService {
}
