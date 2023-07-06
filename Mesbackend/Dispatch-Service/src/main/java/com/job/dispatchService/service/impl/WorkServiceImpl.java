package com.job.dispatchService.service.impl;

import com.job.dispatchService.pojo.TWork;
import com.job.dispatchService.service.WorkBean;
import com.job.dispatchService.service.WorkService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class WorkServiceImpl implements WorkService {
    @GetMapping(value = "/work/{processId}/{orderId}")
    public Object working(@PathVariable("processId") String processId,
                          @PathVariable("orderId") String orderId){
        TWork work = WorkBean.getBean();

        return null;
    }
}
