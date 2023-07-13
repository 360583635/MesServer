package com.job.zuulMaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Users;
import com.job.zuulMaster.mapper.UsersMapper;
import com.job.zuulMaster.service.UsersService;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper,Users> implements UsersService {


}
