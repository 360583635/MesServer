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
    //    首页 查找数据分类
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
        String Id=option+id;//order:1
        System.out.println(id);
        Collection<String> keys = redisCache.keys( option + "*");
        System.out.println(keys);
//        List<Map<String, Object>> resultList = new ArrayList<>();
        Result result = new Result();
        if (id == null) {
//            for (String key : keys) {
//                System.out.println(key);
//                Map<String, Object> cacheMap = redisCache.getCacheMap(key);
//                System.out.println(cacheMap);
//                resultList.add(cacheMap);
//                result.setCode(200);
//                result.setMsg("获取成功");
//                result.setData(resultList);
//                return result;
                result.setCode(200);
                result.setMsg("获取成功");
                result.setData(keys);
                return result;

        } else {
//            for (String key : keys) {
//                while (Id.equals(key)) {
//                    Map<String, Object> cacheMap = redisCache.getCacheMap(key);
//                    System.out.println(cacheMap);
//                    resultList.add(cacheMap);
//                    result.setCode(200);
//                    result.setMsg("获取成功");
//                    result.setData(resultList);
//                    return result;
//                }
                for (String key : keys) {
                    while (Id.equals(key)) {
                        result.setCode(200);
                        result.setMsg("获取成功");
                        result.setData(key);
                        return result;
                    }
            }
        }
        return result;
    }

    //    查找单个数据
    @RequestMapping("/select/{key}")
    public Result findById(@PathVariable(value = "key") String key){
        System.out.println(key);
        Map<String, Object> cacheMap = redisCache.getCacheMap(key);
        System.out.println(cacheMap);
        Result result=new Result();
        result.setData(cacheMap);
        result.setCode(200);
        return result;
    }



    //    删除单个缓存数据
    @RequestMapping("delete/{key}")
    public Result deteById(@PathVariable("key") String key){
        System.out.println(key);
        boolean b = redisCache.deleteObject(key);
        Result result=new Result();
        if (b) {
            result.setMsg("删除成功");
            result.setCode(200);
        } else {
            result.setMsg("删除失败");
            result.setCode(404);
        }
        return result;
    }


    //    设置单个缓存数据过期时间
    @RequestMapping("expire/{key}")
    public Result expireById(@PathVariable("key") String key){
        boolean b = redisCache.expire(key, 0);
        Result result=new Result();
        if (b) {
            result.setMsg("设置过期成功");
            result.setCode(200);
        } else {
            result.setMsg("设置过期失败");
            result.setCode(404);
        }
        return result;
    }

}


