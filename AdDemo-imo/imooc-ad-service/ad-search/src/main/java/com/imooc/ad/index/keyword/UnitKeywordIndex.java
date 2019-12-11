package com.imooc.ad.index.keyword;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 18:21
 * @Description:
 */
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String,Set<Long>> {//根据keyword进行查询更改
    private static Map<String, Set<Long>> keywordUnitMap; //存keyword->unitId集合的映射
    private static Map<Long,Set<String>> unitKeywordMap; //存unitId->关键词集合的映射

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isNotEmpty(key)){
            return Collections.emptySet();
        }

        Set<Long> result = keywordUnitMap.get(key);
        if(result == null){
            return Collections.emptySet();
        }
        return result;
    }

    //todo:这个方法的逻辑不对
    @Override
    public void add(String key, Set<Long> value) { //根据关键字添加unitId(value)到已有的值中
        log.info("UnitKeywordIndex,before add:{}",unitKeywordMap);

        //todo:两个集合没有更新
        //key是keyword
        Set<Long> unitIdSet = CommonUtils.getOrCreate(key,keywordUnitMap,ConcurrentSkipListSet::new);
        unitIdSet.addAll(value);
        if(!unitIdSet.isEmpty()){
            keywordUnitMap.put(key,unitIdSet);
        }

        for(Long unitId : value){
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(key);
            unitKeywordMap.put(unitId,keywordSet);
        }


        log.info("UnitKeyWordIndex,after add:{}",unitKeywordMap);

    }

    //更新复杂不做处理
    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

    }
}
