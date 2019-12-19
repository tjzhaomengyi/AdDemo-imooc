package com.imooc.ad.index.district;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

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


    /**
     * 判断给出的参数districts集合是否是districtsIndex的Map集合中该unit对应distri的子集
     * @param adUnitId
     * @param districts
     * @return
     */
    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts){
        //如果unitDistrictIndex中的map字典中包括该unit并且该unit对应的districts集合不为空
        if(unitDistrictMap.containsKey(adUnitId)
                && CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))){
            //获取该unit在索引中对应的districts集合
            Set<String> unitDistrictsInIndex = unitDistrictMap.get(adUnitId);

            //把需要过滤的参数districts集合转换成index索引形式：ProvinceAndCity内部类 --> province-city字符串格式
            List<String> targetDistricts = districts.stream()
                    .map(d -> CommonUtils.stringConcat(d.getProvince(),d.getCity()))
                    .collect(Collectors.toList());
            //返回targetDistricts与unitDistricts(索引中的districts)匹配的子集
            return CollectionUtils.isSubCollection(targetDistricts,unitDistrictsInIndex);
        }
        return false;
    }

}
