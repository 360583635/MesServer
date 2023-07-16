package com.job.platformService.controller;

import com.job.platformService.config.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import  com.job.platformService.result.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
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
    //    查找单个数据
    @RequestMapping("/selectAll")
    public Result findByIds(){
        Map<String, Object> list = redisCache.getCacheMap("list");
        System.out.println(list);
        Result result=new Result();
        result.setData(list);
        result.setCode(200);
        result.setMsg("查询成功");
        return result;
    }

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


