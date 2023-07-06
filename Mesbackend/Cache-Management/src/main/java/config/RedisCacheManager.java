package config;

import redis.clients.jedis.Jedis;

//Redis 缓存管理类
public class RedisCacheManager {
    private Jedis jedis;

//    创建一个jedis对象
    public RedisCacheManager(String host, int port) {
        jedis = new Jedis(host, port);
    }

//  设置指定键的缓存值。
    public void set(String key, String value) {
        jedis.set(key, value);
    }

//    设置键值对，并指定过期时间expiration。
    public void setex(String key,int expiration, String value){
        jedis.setex(key,expiration,value);
    }

//   设置指定键的缓存值。
    public <T> T get(String key) {
        return (T) jedis.get(key);
    }

//    删除指定键的缓存值。
    public void delete(String key) {
        jedis.del(key);
    }

//    删除指定键组的缓存值。
    public void deletes(String...keys) {
        jedis.del(keys);
    }

//    检查指定键是否存在于缓存中。
    public boolean exists(String key) {
        return jedis.exists(key);
    }

//    关闭与 Redis 的连接。
    public void close() {
        jedis.close();
    }
//    替换缓存（如果存在）
    public void replace(String key,String value){

    }
////    获取多个key的值。
//    public void mget(keys){
//
//    }
////    批量设置多个键值对。
//    public void gset(keys){
//
//    }

}