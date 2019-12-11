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
    public static <K,V> V getOrCreate(K key, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(key,k->factory.get());
    }
}
