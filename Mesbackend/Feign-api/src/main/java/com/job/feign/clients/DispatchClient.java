package com.job.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "DISPATCHSERVICE")
public interface DispatchClient {
    @PostMapping("/dispatch/process/material/queryMaterialsByFlowName")
     Map<String,Integer> queryMaterialsByFlowName(@RequestBody String flowName);
}
