package com.job.dispatchService.Work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Work;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

//@FeignClient(value = "ORDERSERVICE")
public interface WorkService extends IService<Work> {
    public String  insertWork(@Param("processId") String processId, @Param("orderId") String  orderId);

    public String working(@Param("workId") String workId);

    public List<Work> getWorkListByDateTime(@Param("dateTime") String dateTime);

    public List<Work> getAllWorkList();

}
