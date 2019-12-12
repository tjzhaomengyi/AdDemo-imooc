package com.imooc.ad.index.creativeunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 15:46
 * @Description:
 */
@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject> {
    //注意key是adId_unitId
    /**
     * <adId-unitId,CreativeUnitObject>
     */
    private static Map<String,CreativeUnitObject> objectMap;

    /**
     * <adId,unitId set>
     */
    private static Map<Long, Set<Long>> creativeUnitMap;

    /**
     * <unitId,adId set>
     */
    private static Map<Long,Set<Long>> unitCreativeMap;


    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }



    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("before add:{}",objectMap);
        objectMap.put(key,value);

        /**
         * 更新creativeUnitMap
         */
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(),unitSet);
        }
        unitSet.add(value.getUnitId());

        /**
         * 更新unitCreativeMap
         */
        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isEmpty(creativeSet)){
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(),creativeSet);
        }
        creativeSet.add(value.getAdId());
        log.info("after add:{}",objectMap);
    }



    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("CreativeUnitIndex not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("before delete:{}",objectMap);

        objectMap.remove(key);

        /**
         * 删除creativeUnitMap对应
         */
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isNotEmpty(unitSet)){
            unitSet.remove(value.getUnitId());
            //todo:没有更新creativeUnitMap
        }

        /**
         * 删除unitCreativeMap对应
         */
        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(creativeSet)){
            creativeSet.remove(value.getAdId());
            //todo:没有更新unitCreativeMap
        }
        log.info("after delete:{}",objectMap);

    }
}
