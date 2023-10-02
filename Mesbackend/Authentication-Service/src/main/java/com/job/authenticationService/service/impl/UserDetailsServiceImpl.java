package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.pojo.LoginUser;

import com.job.authenticationService.service.UsersService;
import com.job.common.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private MenusMapper menusMapper;

    @Autowired
    private UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("security的UserDetailsService实现类，根据用户名加载用户信息---com.job.authenticationService.service.impl.UserDetailsService");
        log.info("用户名为："+username);
        //根据用户名查询用户信息
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getName, username);
        wrapper.eq(Users::getState,1);
        List<Users> usersList=usersService.list(wrapper);
        log.info("查询到的用户信息为:"+usersList+"---com.job.authenticationService.service.impl.UserDetailsService");
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(usersList)) {
            log.error("users是null");
            throw new RuntimeException("用户名或密码错误");
        }
        Users users=usersList.get(0);
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> permissionKeyList = menusMapper.selectPermsByUserId(users.getId());
       // List<String > permissionKeyList=null;
        log.info("该用户的权限信息："+permissionKeyList+"---com.job.authenticationService.service.impl.UserDetailsService");
        //封装成UserDetails对象返回 
        LoginUser user =  new LoginUser(users,permissionKeyList);
        return user;
    }
}
