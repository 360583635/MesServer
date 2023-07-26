package com.job.feign.clients;

import com.job.feign.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 庸俗可耐
 * @create 2023-07-19-16:39
 * @description
 */
@FeignClient(value = "AUTHENTICATIONSERVICE",url = "http://localhost:6051")
public interface AuthenticationClient {

    @GetMapping("/authen/showdetail/{UserId}")
    public Result showdetail(@PathVariable("UserId") String UserId);
}
