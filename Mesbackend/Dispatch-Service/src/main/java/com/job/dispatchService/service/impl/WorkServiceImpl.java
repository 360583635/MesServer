package com.job.dispatchService.service.impl;

import com.job.dispatchService.mapper.WorkMapper;
import com.job.dispatchService.pojo.Work;
import com.job.dispatchService.service.WorkBean;
import com.job.dispatchService.service.WorkService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class WorkServiceImpl implements WorkService {

    @Resource
    private WorkMapper workMapper;

    @GetMapping(value = "/work/{processId}/{orderId}")
    public Object working(@PathVariable("processId") String processId,
                          @PathVariable("orderId") String orderId){
        Work work = WorkBean.getWork();
        workMapper.insert(work);
        return null;
    }
}
