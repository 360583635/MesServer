package com.job.dispatchService.Work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Work;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

//@FeignClient(value = "ORDERSERVICE")
public interface WorkService extends IService<Work> {
    public String  insertWork(String processId, String  orderId);

    public String working(String workId);

    public List<Work> getWorkList(String dateTime);
}
