package com.imooc.ad.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 18:37
 * @Description:
 */
@Slf4j
public class CommonUtils {
    /**
     * 如果Map存在key，返回这个key的集合；如果不存在建立一个空的factory
     * @param key
     * @param map
     * @param factory
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> V getOrCreate(K key, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(key,k->factory.get());
    }

    public static String stringConcat(String...args){
        StringBuilder result = new StringBuilder();
        for(String arg:args){
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
}
