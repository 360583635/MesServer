package com.job.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-20-15:13
 * @description
 */
@FeignClient(value = "DISPATCHSERVICE")
public interface DispatchClient {
    @PostMapping("/dispatch/process/material/queryMaterialsByFlowName")
    public List<String> queryMaterialsByFlowName(@RequestBody String flowName);
}
