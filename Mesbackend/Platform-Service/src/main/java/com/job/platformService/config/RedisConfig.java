package com.job.platformService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//用于配置 RedisTemplate 的 RedisConfig 类。在这个类中，你可以设置 RedisTemplate 的序列化方式、连接工厂等配置。
@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
//        创建redistemplate对象
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        将连接工厂设置到redistemplate中以便与redis通信
        template.setConnectionFactory(connectionFactory);
//        创建一个 FastJsonRedisSerializer 对象，并将其中传入的 Class 设置为 Object.class，表示要序列化和反序列化的对象类型是 Object。
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

//        使用StringRedisSerializer来序列化和反序列化redis的key值
//        设置键的序列化器为 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
//        使用 setHashValueSerializer 方法设置哈希值的序列化器为上面创建的 FastJsonRedisSerializer。
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

//      afterPropertiesSet() 方法用于确保 RedisTemplate 的所有属性都已经设置完毕，并作为重要的初始化步骤。该方法会执行一些必要的校验和配置，并保证 RedisTemplate 可以正常运行。
        template.afterPropertiesSet();
        return template;
    }
}

