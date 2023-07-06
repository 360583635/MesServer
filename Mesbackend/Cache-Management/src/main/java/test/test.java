package test;

import config.JedisPoolUtils;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import redis.clients.jedis.Jedis;

public class test {
    private Jedis jedis;

    @BeforeEach
    void setup(){
//        调用连接池获取连接池对象
         jedis = JedisPoolUtils.getJedis();

    }

    @Test
    public void testString(){
        System.out.println(jedis.get("xxx"));
    }


    @AfterEach
    void setdown(){
        if (jedis != null){
            jedis.close();
        }
    }

    //通过java程序访问redis数据库
//    @Test
    //获得单一的jedis对象操作数据库
//    public void test1(){
//
//        //1.获得连接对象
//        Jedis jedis = new Jedis("127.0.0.1", 6379);
//
//        //2.获得数据
//        String username = jedis.get("name");
//        System.out.println(username);
//
//        //3.存储
//        jedis.set("addr","北京");
//        System.out.println(jedis.get("addr"));
//    }

//    通过jedis的pool获得jedis连接对象
//    @Test
//    public void test2(){
//        //0.创建池子的配置对象
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        //最大空闲时间
//        poolConfig.setMaxIdle(30);
//        poolConfig.setMinIdle(10);
//        //最大连接数
//        poolConfig.setMaxTotal(50);
//        //1.创建一个redis的连接池
//        JedisPool pool = new JedisPool(poolConfig,"127.0.0.1", 6379);
//        //2.从池子中获取redis的连接资源
//        Jedis jedis = pool.getResource();
//
//        //3.操作数据库
//        jedis.set("xxx", "yyy");
//        System.out.println(jedis.get("xxx"));
//
//        //4.关闭资源
//        jedis.close();
//        pool.close();

    }

