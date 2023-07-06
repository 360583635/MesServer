package config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisPoolUtils {

        private static JedisPool pool = null;

        static{
            //加载配置文件
            InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("application.properties");
            Properties pro = new Properties();
            try {
                pro.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //获得池子的配置对象
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxIdle").toString()));
            poolConfig.setMinIdle(Integer.parseInt(pro.get("redis.minIdle").toString()));
            poolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxTotal").toString()));
            String url = pro.get("redis.url").toString();
            Integer port = Integer.parseInt(pro.get("redis.port").toString());
//            创建一个连接池对象
            pool = new JedisPool(poolConfig,url,port);
        }

    //获得jedis资源的方法 创建jedis对象的方法
    public static Jedis getJedis(){
        return pool.getResource();
    }
    }

