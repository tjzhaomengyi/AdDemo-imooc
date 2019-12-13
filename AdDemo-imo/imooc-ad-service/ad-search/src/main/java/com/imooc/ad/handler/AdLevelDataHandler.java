package com.imooc.ad.handler;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/13 14:06
 * @Description:将AdTable中获得的数据转变为index索引，实现索引的增删改
 * 1、索引之间存在层级的划分，也就是依赖关系的划分
 * 2、加载全量的索引其实是增量索引“增加”的一种特殊实现
 * 3、根据表与表之间分为2/3/4级别的索引，2级表示和其他表没有关联
 */
@Slf4j
public class AdLevelDataHandler {


    /**
     * 广告计划索引操作
     * @param planTable
     * @param type
     */
    public static void handleLevel2(AdPlanTable planTable,OpType type){
        AdPlanObject planObject = new AdPlanObject(planTable.getId(),planTable.getUserId(),
                planTable.getPlanStatus(),planTable.getStartDate(),planTable.getEndDate());

        handleBinlogEvent(DataTable.of(AdPlanIndex.class),planObject.getPlanId(),planObject,type);
    }

    /**
     * 创意索引
     * @param creativeTable
     * @param type
     */
    public static void handlerLevel2(AdCreativeTable creativeTable,OpType type){
        CreativeObject creativeObject = new CreativeObject(creativeTable.getAdId(),creativeTable.getName(),
                creativeTable.getType(),creativeTable.getMaterialType(),
                creativeTable.getHeight(),creativeTable.getWidth(),
                creativeTable.getAuditStatus(),creativeTable.getAdUrl());

        handleBinlogEvent(DataTable.of(CreativeIndex.class),creativeObject.getAdId(),creativeObject,type);
    }


    /**
     * 广告单元，unit需要关联到adPlan，如果adPlan不存在，这个Unit就不能被加载到索引
     * @param unitTable
     * @param type
     */
    public static void hanleLevel3(AdUnitTable unitTable,OpType type){
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());//在AdPlan中查询有没有这个unittable中unit的planId
        if(null == adPlanObject){ //如果根据这个planId在索引中找不到对应的adPlanObject索引，那么说明这个推广单元没有推广计划上线，我们就不应该加载到索引中
            log.error("AdUnitTable hanleLevel3 found AdPlanObject error:{}",unitTable.getPlanId());
            return;
        }

        AdUnitObject adUnitObject = new AdUnitObject(unitTable.getUnitId(),unitTable.getUnitStatus(),
                unitTable.getPositionType(),unitTable.getPlanId(),adPlanObject);
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),adUnitObject.getUnitId(),adUnitObject,type);

    }

    /**
     * 创意和推广单元，需要unit和creative都存在，才能建立索引
     * @param creativeUnitTable
     * @param type
     */
    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable,OpType type){
        if(type == OpType.UPDATE){ //todo:这个是否是多余
            log.error("CreativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());

        if(null == unitObject || null == creativeObject) {//和上面一样，如果不存在unit和creative的索引，我们就不增加这个索引
            log.error("AdCreativeUnitTable index error:{}", JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(),creativeUnitTable.getUnitId());

        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class),//转换的目标索引对象类型
                CommonUtils.stringConcat(creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()),//注意这里的索引是creative的adid-unitId);
                creativeUnitObject,
                type);
    }

    /**
     * 广告推广单元区域限制 table转index对象
     * @param unitDistrictTable
     * @param type
     */
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable,OpType type){
        if(type == OpType.UPDATE){
            log.error("district index cannot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if(null == unitObject){
            log.error("AdUnitDistrictTable index error:{}",unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(unitDistrictTable.getProvince(),unitDistrictTable.getCity()); //unitDistrict连接是province-city
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));//这里Table中就对应一个数据，所以对应索引也是一个元素的hashset
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class),key,value,type);
    }

    /**
     * 广告推广单元兴趣限制 table转index对象
     * @param unitItTable
     * @param type
     */
    public static void handleLevel4(AdUnitItTable unitItTable,OpType type){
        if(type == OpType.UPDATE){
            log.error("it index cannot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if(unitObject == null){
            log.error("AdUnitItTable index error:{}",unitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitItIndex.class),unitItTable.getItTag(),value,type);//unitItIndex的格式是ItTag：unitId<Set>

    }

    /**
     * 广告推广单元关键词限制 table转index对象
     * @param keywordTable
     * @param type
     */
    public static void handleLevel4(AdUnitKeywordTable keywordTable,OpType type){
        if(type == OpType.UPDATE){
            log.error("keyword index cannnot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if(unitObject == null){
            log.error("AdUnitKeywordTable index error:{}",keywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(Collections.singleton(keywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class),keywordTable.getKeyword(),value,type);
    }



    /**
     *    既可以处理增量处理过程，又可以处理全量处理过程
     *    IndexAware是AdSearch中的索引对象
     *    K表示操作索引的key，V表示操作索引的对象
     *    type表示执行哪种操作
     */
    private static <K,V> void handleBinlogEvent(IndexAware<K,V> index, K key, V value, OpType type){
        switch (type){
            case ADD:
                index.add(key,value);
                break;
            case UPDATE:
                index.update(key,value);
            case DELETE:
                index.delete(key,value);
            case OTHER:
                break;
            default:
                break;
        }
    }
}
