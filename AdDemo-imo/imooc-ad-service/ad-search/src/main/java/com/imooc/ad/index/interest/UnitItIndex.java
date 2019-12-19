package com.imooc.ad.index.interest;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 11:57
 * @Description:
 */
@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> { //爱好和unit关联索引

    /**
     * <itTag,adUnitId set>兴趣爱好和unit推广单元关联索引
     */
    private static Map<String,Set<Long>> itUnitMap;

    /**
     * <adUnitId,itTag set>某个推广单元对应的所有兴趣标签索引
     */
    private static Map<Long,Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isNotEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> result = itUnitMap.get(key);
        if(result == null){
            return  Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        //todo:更新两个正反向索引map
        log.info("UnitItIndex,before add:{}",unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key,itUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        for(Long unitId:value){
            Set<String> its = CommonUtils.getOrCreate(unitId,unitItMap,ConcurrentSkipListSet::new);
            its.add(key);
        }

        log.info("UnitIndex,after add:{}",unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("unitItIndex,before delete:{}",unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key,itUnitMap,ConcurrentSkipListSet::new);
        unitIds.removeAll(value);

        for(Long unitId:value){
            Set<String> itTagSet = CommonUtils.getOrCreate(unitId,unitItMap,ConcurrentSkipListSet::new);
            itTagSet.remove(key);
        }
        log.info("unitItIndex,after delete:{}",unitItMap);
    }

    /**
     * 根据符合UnitIndex中匹配unitId的map索引，过滤传进来的itTags
     * @param unitId
     * @param itTags
     * @return
     */
    public boolean match(Long unitId, List<String> itTags){
        if(unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitItMap.get(unitId))){
            Set<String> unitKeywords = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags,unitKeywords);
        }
        return false;
    }
}
