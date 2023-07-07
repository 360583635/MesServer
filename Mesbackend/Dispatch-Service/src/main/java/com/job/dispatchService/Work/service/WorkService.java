package com.job.dispatchService.Work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.dispatchService.Work.pojo.Work;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ORDERSERVICE")
public interface WorkService extends IService<Work> {
}
