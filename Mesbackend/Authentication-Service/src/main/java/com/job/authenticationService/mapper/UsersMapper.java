package com.job.authenticationService.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.job.common.pojo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
@Mapper
@Repository
public interface UsersMapper extends BaseMapper<Users> {
    Users selectOne(LambdaQueryWrapper<Users> queryWrapper);
}

