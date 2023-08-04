package com.job.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "DISPATCHSERVICE",url = "http://localhost:6031")
public interface DispatchClient {
    @PostMapping("/dispatch/process/material/queryMaterialsByFlowName")
     Map<String,Integer> queryMaterialsByFlowName(@RequestHeader("token") String token,@RequestBody Map<String,String> map);
}
