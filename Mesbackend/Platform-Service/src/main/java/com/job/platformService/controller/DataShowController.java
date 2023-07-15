package com.job.platformService.controller;

import com.job.common.result.Result;
import com.job.platformService.config.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class DataShowController {
    @Autowired
    private RedisCache redisCache;

    /*
    查找数据
     */

    @RequestMapping("select")
    public Result select(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "option") String option) {
        String Id=option+id;
        System.out.println(Id);
        Collection<String> keys = redisCache.keys( option + "*");
        System.out.println(keys);
        List<Map<String, Object>> resultList = new ArrayList<>();
        Result result = new Result();
        if (Id == null) {
            for (String key : keys) {
                System.out.println(key);
                Map<String, Object> cacheMap = redisCache.getCacheMap(key);
                System.out.println(cacheMap);
                resultList.add(cacheMap);
                result.setCode(200);
                result.setMsg("获取成功");
                result.setData(resultList);
                return result;
            }
        } else {
            for (String key : keys) {
                while (Id.equals(key)) {
                    Map<String, Object> cacheMap = redisCache.getCacheMap(key);
                    System.out.println(cacheMap);
                    resultList.add(cacheMap);
                    result.setCode(200);
                    result.setMsg("获取成功");
                    result.setData(resultList);
                    return result;
                }
            }
        }
        return result;

    }

}


