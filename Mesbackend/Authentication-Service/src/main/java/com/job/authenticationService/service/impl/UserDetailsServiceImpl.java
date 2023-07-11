package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.pojo.LoginUser;
import com.job.authenticationService.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private MenusMapper menusMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("9090");
        System.out.println(username);
        //根据用户名查询用户信息
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getName, username);
        Users users = usersMapper.selectOne(wrapper);
        System.out.println(users);
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(users)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> permissionKeyList = menusMapper.selectPermsByUserId(users.getId());
       // List<String > permissionKeyList=null;
        System.out.println(permissionKeyList);


        //封装成UserDetails对象返回 
        return new LoginUser(users,permissionKeyList);
    }
}
