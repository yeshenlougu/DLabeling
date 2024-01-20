package com.dlabeling.framework.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.dlabeling.common.constant.Constants;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Description: Redis使用FastJson序列化
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2023/12/7
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);
    
    private Class<T> clazz;
    
    public FastJson2JsonRedisSerializer(Class<T> clazz){
        super();
        this.clazz = clazz;
    }
    
    @Override
    public byte[] serialize(T t) throws SerializationException{
        if (t == null){
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }
    
    @Override
    public T deserialize(byte[] bytes) throws SerializationException{
        if (bytes == null || bytes.length <= 0){
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
    
}