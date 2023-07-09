package com.job.platformService.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 *  FastJson 序列化和反序列化对象的 RedisSerializer 实现类。它实现了 RedisSerializer 接口，用于在 Redis 上存储和获取对象。
 * @author sg
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>
{

//    设置编码格式
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

//    使用了 FastJson 的 ParserConfig，将自动类型支持设置为 true，以支持反序列化时的自动类型转换。
    static
    {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }


//    方法将对象 t 序列化为字节数组。如果传入的对象为 null，返回一个空字节数组；否则，使用 FastJson 的 toJSONString 方法将对象转换为 JSON 字符串，并将其转换为字节数组。
    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

//    方法将字节数组 bytes 反序列化为对象。如果传入的字节数组为 null 或长度为 0，返回 null；否则，将字节数组转换为字符串，并使用 FastJson 的 parseObject 方法将字符串解析为对象。
    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }

// 返回一个 JavaType 对象，用于将 Class 对象转换为 FastJson 中的 JavaType。
    protected JavaType getJavaType(Class<?> clazz)
    {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}