package com.job.dispatchService.Work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.job.dispatchService.Work.mapper.WorkMapper;
import com.job.dispatchService.Work.pojo.Work;
import com.job.dispatchService.Work.service.WorkBean;
import com.job.dispatchService.Work.service.WorkService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper,Work> implements WorkService {

    @Resource
    private WorkMapper workMapper;

//    @Resource
//    private Order

    @GetMapping(value = "/work/{processId}/{orderId}")
    public Object working(@PathVariable("processId") String processId,
                          @PathVariable("orderId") String orderId){
        Work work = WorkBean.getWork();
        workMapper.insert(work);
        return null;
    }
}
