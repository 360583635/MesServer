package com.zx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.mapper.UserMapper;
import com.zx.pojo.User;
import com.zx.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author 菜狗
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
