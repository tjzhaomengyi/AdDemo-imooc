package com.imooc.ad.index.district;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 14:49
 * @Description:
 */

@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String,Set<Long>> {
    //倒排索引
    private static Map<String, Set<Long>> distirctUnitMap;

    //正向索引
    private static Map<Long,Set<String>> unitDistrictMap;

    static {
        distirctUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        return distirctUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitDistrictIndex,before add:{}",unitDistrictMap);

        //todo:没有更新两个Map
        Set<Long> unitIds = CommonUtils.getOrCreate(key,distirctUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        for(Long unitId:value){
            Set<String> districts = CommonUtils.getOrCreate(unitId,unitDistrictMap,ConcurrentSkipListSet::new);
            districts.add(key);
        }

        log.info("UnitDistrictIndex,after add :{}",unitDistrictMap);

    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("distirct index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitDistrictIndex,befor delete:{}",unitDistrictMap);
        //todo:更新对应map
        Set<Long> unitIds = CommonUtils.getOrCreate(key,distirctUnitMap,ConcurrentSkipListSet::new);
        unitIds.removeAll(value);

        for(Long unitId:value){
            Set<String> districts = CommonUtils.getOrCreate(unitId,unitDistrictMap,ConcurrentSkipListSet::new);
            districts.remove(key);
        }
        log.info("UnitDistrictIndex,after delete:{}",unitDistrictMap);
    }

}
