package com.imooc.ad.index.adunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 17:22
 * @Description:AdUnit索引
 */
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long,AdUnitObject> {

    private static Map<Long,AdUnitObject> objectMap;

    static {
        //加载后objectMap索引对象加载进来了，有数据
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("before update:{}",objectMap);
        AdUnitObject oldObject = objectMap.get(key);
        if(null == oldObject){
            objectMap.put(key,value);
        } else {
            oldObject.update(value);
        }
        log.info("after update:{}",objectMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }

    /**
     * 查找positionType符合的Unit,传进来的postionType是否和获取到的positionType一致
     * @param positionType
     * @return
     */
    public Set<Long> match(Integer positionType){
        Set<Long> adUnitIds = new HashSet<>();
        objectMap.forEach((k,v)->{
            if(AdUnitObject.isAdSlotTypeOk(positionType,v.getPositionType())){
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }

    /**
     * 根据UnitIds筛选对应的UnitObject
     * @param adUnitIds
     * @return
     */
    public List<AdUnitObject> fetch(Collection<Long> adUnitIds){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return Collections.emptyList();
        }
        List<AdUnitObject> result = new ArrayList<>();
        adUnitIds.forEach(u ->{
            AdUnitObject unitObject = get(u);
            if(null == unitObject){
                log.error("AdUnitObject not Found:{}",u);
            }
            result.add(unitObject);
        });
        return result;
    }

}
