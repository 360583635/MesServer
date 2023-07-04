package com.job.zuulMaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.zuulMaster.mapper.UserMapper;
import com.job.zuulMaster.pojo.User;
import com.job.zuulMaster.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author 菜狗
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
