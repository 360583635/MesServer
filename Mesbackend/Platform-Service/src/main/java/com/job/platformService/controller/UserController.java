package com.job.platformService.controller;

import com.job.platformService.config.RedisCache;
import com.job.platformService.pojo.MyDTO;
import com.job.platformService.pojo.User;
import com.job.platformService.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/cache")
public class UserController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    添加缓存数据分类
*/
    @RequestMapping("list")
    public Result addList(){
//        redisCache.setCacheMapValue("list","user","用户数据");

        redisCache.addList("order:","订单管理");
        Map<String,String> map=new HashMap<>();
        map.put("key","order1");
        map.put("user","zyx");
        map.put("password","123456");
        redisCache.setCacheMap("order:1",map);

        Map<String,String> map1=new HashMap<>();
        map.put("key","order2");
        map.put("user","zyx");
        map.put("password","123456");
        User user=new User();
        user.setUsername("zyx");
        user.setPassword("1234");
        map.put("user", String.valueOf(user));
        redisCache.setCacheMap("order:2",map);

        Collection<String> order = redisCache.keys("order"+"*");
        System.out.println(order);

        Map<String, Object> cacheMap = redisCache.getCacheMap("order:1");
        System.out.println(cacheMap);
        return null;


    }



//

    //    查找单个数据
    @RequestMapping("/select/{key}")
    public String findById(@PathVariable("key") String key){
        System.out.println(key);
        Object obj = key; // 要获取数据类型的对象
        if (obj instanceof Integer) {
            System.out.println("数据类型：Integer");
        } else if (obj instanceof String) {
            System.out.println("数据类型：String");
        } else if (obj instanceof Double) {
            System.out.println("数据类型：Double");
        } // 其他类型判断...
        String s = redisCache.getCacheObject("user:22");
        System.out.println(s);
        return s;
    }

    //    获得某个前缀的所有键的对象列表  模糊匹配
    @RequestMapping("keys/{pattern}")
    public Collection keys(@PathVariable("pattern") String pattern){
        System.out.println(pattern);
        Collection<String> keys = redisCache.keys("*"+pattern+"*");
        System.out.println(keys);
        List<String> dataList = redisTemplate.opsForValue().multiGet(keys);
        System.out.println(dataList);
//        for (String key:keys){
//            System.out.println(key);
//            Object value=redisCache.getCacheObject(key);
//            System.out.println(value);
//        }
        return dataList;
    }

        //    添加数据
    @RequestMapping("/add")
    public void add(){
        System.out.println(111);
        User user=new User();
        user.setUsername("zyx");
        user.setPassword("zyx");
        User user1=new User();
        user1.setUsername("ysx");
        user1.setPassword("ysx");
        User user2=new User();
        user2.setUsername("faq");
        user2.setPassword("faq");

        redisCache.setCacheObject("user:1",user);
        redisCache.setCacheObject("user:2",user1);
        redisCache.setCacheObject("user:3",user2);
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


    //    查看单个缓存数据过期时间
    @RequestMapping("ttl/{key}")
    public Long ttlById(@PathVariable("key") String key){
        System.out.println(key);
        long cacheExpiration = redisCache.getCacheExpiration("key");
        System.out.println(cacheExpiration);
        Long expire = redisTemplate.getExpire("user:22");

        return expire;
    }


    //    设置单个缓存数据过期时间
    @RequestMapping("expire/{key}")
    public boolean expireById(@PathVariable("key") String key){
        boolean expire = redisCache.expire(key, 2);
        System.out.println(expire);
        return expire;
    }

    //    设置多个缓存数据过期时间  批量失效
    @RequestMapping("expires")
    public boolean expireById(@RequestBody MyDTO data){
//        System.out.println(data);//com.job.platformService.pojo.MyDTO@cff4b67
        // 处理接收到的JSON数据
        String[] selectedItems = data.getSelectedItems();
//        System.out.println(selectedItems);//[Ljava.lang.String;@4ff33d3
//        System.out.println(Arrays.toString(selectedItems));//[key1, key2, key3]
        List<String> keylist= Arrays.asList(selectedItems);
        boolean expire = false;

        for (String key:keylist) {
            System.out.println(key);
             expire = redisCache.expire(key, 2);
        }
        return expire;
    }


//    添加hash数据
    @RequestMapping("/hash")
    public void hash(){
//        redisCache.setCacheMapValue("order","userid",12345);
//        redisCache.setCacheMapValue("order","username",12345);
//        redisCache.setCacheMapValue("order","price",12345);
//        Map<String, Object> order = redisCache.getCacheMap("order");
//        System.out.println(order);

//        Map<String,Integer> dataMap=new HashMap<>();
//        dataMap.put("filed1",1);
//        dataMap.put("filed2",1);
//        dataMap.put("filed3",1);
//        redisCache.setCacheMap("myhash",dataMap);
//        Object cacheMapValue = redisCache.getCacheMapValue("myhash", "filed1");
//        System.out.println(cacheMapValue);

        redisCache.setCacheMapValue("user:zyx1","username",12345);
        redisCache.setCacheMapValue("user:zyx1","password",12345);
        Map<String, User> order = redisCache.getCacheMap("user:zyx1");
        System.out.println(order);
    }


//    批量删除hash数据
        @PostMapping("deletehash")
        public long deletehashbyids(@RequestBody MyDTO data){
//        System.out.println(data);//com.job.platformService.pojo.MyDTO@cff4b67
    // 处理接收到的JSON数据
        String[] selectedItems = data.getSelectedItems();
//        System.out.println(selectedItems);//[Ljava.lang.String;@4ff33d3
//        System.out.println(Arrays.toString(selectedItems));//[key1, key2, key3]
        List<String> keylist= Arrays.asList(selectedItems);
        long l = redisCache.deleteObject(keylist);
        return l;
}


//  获取使用缓存空间大小
    @RequestMapping("ca")
    public long caculateCacheSize(){
        RedisConnection connection=redisTemplate.getConnectionFactory().getConnection();
//        执行命令获取缓存大小
        Long cacheSize=connection.dbSize();
        connection.close();
        return cacheSize;
    }







//    添加数据
    @RequestMapping("stamp")
    public Result getDataWithTimestamp() {
        // 时间字符串
        String timeString = "2023-07-09 14:30:00";
// 时间字符串转换为日期时间对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
// 日期时间对象转换为时间戳（秒级）
        long timestamp1 = dateTime.toEpochSecond(ZoneOffset.UTC);
        System.out.println("Timestamp (seconds): " + timestamp1);

        // 构建数据
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("user", "zyx");
        dataMap.put("password", "12345");

        // 向Redis存储数据，并设置时间戳字段   数据以哈希类型（Hash）的方式存储
        redisTemplate.opsForHash().putAll("data1", dataMap);
        redisTemplate.opsForHash().put("data1", "timestamp", timestamp1);
        return null;
    }

//    根据时间查询数据
@RequestMapping("data")
    public String getHashData(String hashKey) {
//    // 创建日期时间对象
//    LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 9, 14, 30, 0);
//    // 创建日期时间格式化对象
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    System.out.println(formatter);
//    // 将时间对象转换为字符串
//    String timeString1 = dateTime1.format(formatter);
//    System.out.println(timeString1);

    // 获取当前时间
    LocalDateTime currentTime = LocalDateTime.now();
    System.out.println(currentTime);
    // 将时间转换为时间戳（秒级）
    long timestamp = currentTime.toEpochSecond(ZoneOffset.UTC);
    System.out.println(timestamp);


// 时间字符串
    String timeString = "2023-07-11 14:30:00";
// 时间字符串转换为日期时间对象
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
// 日期时间对象转换为时间戳（秒级）
    long timestamp1 = dateTime.toEpochSecond(ZoneOffset.UTC);
    System.out.println("Timestamp (seconds): " + timestamp1);


// 时间字符串
    String timeString2= "2023-07-12 14:30:00";
// 时间字符串转换为日期时间对象

    LocalDateTime dateTime2 = LocalDateTime.parse(timeString2, formatter);
// 日期时间对象转换为时间戳（秒级）
    long timestamp2 = dateTime2.toEpochSecond(ZoneOffset.UTC);
    System.out.println("Timestamp (seconds): " + timestamp2);


    // 获取HashOperations对象
    HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
    // 定义匹配模式
    String pattern = "data";
    // 获取所有符合模式的键值对
    Collection<String> keys = redisCache.keys("*"+pattern+"*");
    System.out.println(keys);

    for (String key : keys) {
        long time = redisCache.getCacheMapValue(key, "timestamp");
        System.out.println(time);
        if (time >= timestamp1 && time <= timestamp2) {
            System.out.println("Key: " + key + ", Value: " + time);
        }

    }
   return null;
    }






}
