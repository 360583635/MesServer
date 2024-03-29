package com.job.orderService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Flow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-17:44
 * @description
 */
@Mapper
public interface FlowMapper extends BaseMapper<Flow> {
}
