package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.service.UsersService;
import com.job.common.pojo.Users;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
}
