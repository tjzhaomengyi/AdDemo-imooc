package com.imooc.ad.index.adplan;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 16:51
 * @Description:AdPlan正向索引
 */
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {

    private static Map<Long,AdPlanObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();//静态对应，在堆里，每次ApPlanIndex对象的操作都把数据塞到这个objectMap中
    }

    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add:{}",objectMap);
        objectMap.put(key,value);
        log.info("after add:{}",objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update:{}",objectMap);
        AdPlanObject oldObject = objectMap.get(key);
        if(null == oldObject){
            objectMap.put(key,value);
        } else {
            oldObject.update(value);
        }
        log.info("after update:{}",objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }
}
