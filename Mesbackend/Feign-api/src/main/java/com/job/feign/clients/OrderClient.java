package com.job.feign.clients;

import com.job.common.pojo.Order;
import com.job.feign.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 庸俗可耐
 * @create 2023-08-04-22:53
 * @description
 */
@FeignClient(value = "ORDERSERVICE",url = "http://localhost:6021")
public interface OrderClient {

    @PostMapping("/updateByOne")
    public Result<Order> updateByOne(Order order);
}
