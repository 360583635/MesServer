package com.job.platformService.controller;

import com.job.platformService.config.RedisCache;
import com.job.platformService.pojo.MyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import  com.job.platformService.result.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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


    //    删除多个数据
    @PostMapping("deletes")
    public long deletebyids(@RequestBody MyDTO data){
//        System.out.println(data);//com.job.platformService.pojo.MyDTO@cff4b67
        // 处理接收到的JSON数据
        String[] selectedItems = data.getSelectedItems();
//        System.out.println(selectedItems);//[Ljava.lang.String;@4ff33d3
//        System.out.println(Arrays.toString(selectedItems));//[key1, key2, key3]
        List<String> keylist= Arrays.asList(selectedItems);
        long l = redisCache.deleteObject(keylist);
        return l;
    }

}


