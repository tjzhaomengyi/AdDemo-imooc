package com.imooc.ad.index;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 16:26
 * @Description:索引对象
 */
public interface IndexAware<K,V> {

    V get(K key);

    void add(K key,V value);

    void update(K key,V value);

    void delete(K key,V value);
}
