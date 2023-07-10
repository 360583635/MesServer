package com.job.platformService.controller;

import com.job.platformService.config.RedisCache;
import com.job.platformService.pojo.MyDTO;
import com.job.platformService.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/cache")
public class UserController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

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

    //    删除单个缓存数据
    @RequestMapping("delete/{key}")
    public String deteById(@PathVariable("key") String key){
        boolean b = redisCache.deleteObject(key);
        if (b) {
            return "Key deleted successfully";
        } else {
            return "Key not found or deletion failed";
        }

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


    @RequestMapping("ca1")
    public void exportCacheDataAsCSV(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("D:\\导出数据\\1\\2.csv"))) {
            redisTemplate.execute((RedisCallback<Void>) connection -> {
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("*").count(1000).build());
                while (cursor.hasNext()) {
                    byte[] keyBytes = cursor.next();
                    byte[] valueBytes = connection.get(keyBytes);
                    String key = new String(keyBytes);
                    String value = new String(valueBytes);
                    writer.write(key + "," + value + "\n");
                }
                cursor.close();
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file writer", e);
        }
    }



}
