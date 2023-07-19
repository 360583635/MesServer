package com.job.orderService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Line;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-17:59
 * @description
 */
@Mapper
public interface LineMapper extends BaseMapper<Line> {
}
