package com.job.dispatchService.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Flow;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
public interface WFlowMapper extends BaseMapper<Flow> {
}
