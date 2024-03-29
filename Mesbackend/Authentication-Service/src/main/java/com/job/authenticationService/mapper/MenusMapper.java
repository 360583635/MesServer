package com.job.authenticationService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Menus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MenusMapper extends BaseMapper<Menus> {
    List<String> selectPermsByUserId(String  id);

}
