package com.imooc.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.CommonStatus;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 18:05
 * @Description:实现ad查询
 */
@Slf4j
@Service
public class SearchImpl implements ISearch {

    //定义HystrixCommand对应的fallback方法
    public SearchResponse fallback(SearchRequest request,Throwable e){
        return null;
    }

    @HystrixCommand(fallbackMethod = "fallback")//这个方法很少使用，性能较低
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        //获取request中的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        //获取Feature请求信息
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        //构造响应对象
        SearchResponse response = new SearchResponse();
        Map<String,List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();


        for(AdSlot adSlot : adSlots){
            Set<Long> targetUnitIdSets;

            //1、根据广告位的流量类型获取初始AdUnit，过滤流量类型
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            //2、过滤三个条件过滤，分为AND关系和OR关系
            if(relation == FeatureRelation.AND){
                //交集对adUnitIdSet根据Feature进行层层过滤
                filterKeywordFeature(adUnitIdSet, keywordFeature);//过滤出符合keyword条件的unitId
                filterDistrictFeature(adUnitIdSet, districtFeature);//过滤出符合district条件的unitId
                filterItTagFeature(adUnitIdSet, itFeature);//过滤出符合ItTag条件的UnitID

                targetUnitIdSets = adUnitIdSet;
            } else {
                targetUnitIdSets = getORRelationUnitIds(adUnitIdSet,keywordFeature,districtFeature,itFeature);
            }

            //3、根据unitIdSet获取unitObjects对象集合
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSets);
            //4、过滤上线状态的unitObject
            filterAdUnitAndPlanStatus(unitObjects,CommonStatus.VALID);

            //5、通过CreativeUnitIndex索引找到Unit推广单元对应的创意CreativeId集合
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            //6、通过adIds获取广告创意对象CreativeObjects
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(adIds);

            //6、通过AdSlot实现对CreativeObjects的过滤，匹配广告位的width、height、type
            filterCreativeByAdSlot(creativeObjects,adSlot.getWidth(),adSlot.getHeight(),adSlot.getType());

            //7、一个广告位只返回一个广告创意信息
            adSlot2Ads.put(adSlot.getAdSlotCode(),buildCreativeResponse(creativeObjects));
        }
        log.info("fetchAds: {}-{}", JSON.toJSONString(request),JSON.toJSONString(response));
        return response;
    }

    /**
     * 过滤Keyword Feature,根据keywordFeature中过滤出符合条件的adUnitIds子集
     * @param adUnitIds
     * @param keywordFeature
     */
    private void filterKeywordFeature(Collection<Long> adUnitIds,KeywordFeature keywordFeature){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){
            //加载unitkeyword的index，里面有对应map集合
            UnitKeywordIndex unitKeywordIndex = DataTable.of(UnitKeywordIndex.class);

            //使用unitKeywordIndex对象中的map集合过滤符合keywordFeature的unitId
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> unitKeywordIndex.match(adUnitId,keywordFeature.getKeywords()));
        }
    }

    /**
     * 根据districtFeature过滤符adUnitIds中符合条件的子集
     * @param adUnitIds
     * @param districtFeature
     */
    private void filterDistrictFeature(Collection<Long> adUnitIds,DistrictFeature districtFeature){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            UnitDistrictIndex unitDistrictIndex = DataTable.of(UnitDistrictIndex.class);
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> unitDistrictIndex.match(adUnitId,districtFeature.getDistricts()));
        }
    }

    private void filterItTagFeature(Collection<Long> adUnitIds,ItFeature itFeature){
        if(CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(itFeature.getIts())){
            UnitItIndex unitItIndex = DataTable.of(UnitItIndex.class);
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> unitItIndex.match(adUnitId,itFeature.getIts()));
        }
    }

    /**
     * 根据keyword、districts和it限制条件，找到符合任一条件的adUnitIdSet子集集合
     * @param adUnitIdSet
     * @param keywordFeature
     * @param districtFeature
     * @param itFeature
     * @return
     */
    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,ItFeature itFeature){
        if(CollectionUtils.isEmpty(adUnitIdSet)){
            return Collections.emptySet();
        }
        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet,keywordFeature);
        filterDistrictFeature(districtUnitIdSet,districtFeature);
        filterItTagFeature(itUnitIdSet,itFeature);

        return new HashSet<>(CollectionUtils.union(
                CollectionUtils.union(keywordUnitIdSet,districtUnitIdSet),itUnitIdSet));
    }

    /**
     * 从unitObjects中过滤status状态的推广单元
     * @param unitObjects
     * @param status
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status){
        if(CollectionUtils.isEmpty(unitObjects)){
            return;
        }
        //要求Unit状态和AdPlan状态同时满足
        CollectionUtils.filter(unitObjects,
                object -> object.getUnitStatus().equals(status.getStatus())
                        && object.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }

    /**
     * 过滤与广告位AdSlot匹配的广告创意AdCreative
     * @param creativeObjects
     * @param width
     * @param height
     * @param type
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,Integer width,Integer height,List<Integer> type){
        if(CollectionUtils.isEmpty(creativeObjects)){
            return;
        }

        //过滤和广告为匹配的CreativeObjects
        CollectionUtils.filter(creativeObjects,creative ->
                creative.getAuditStatus().equals(CommonStatus.VALID.getStatus())
        && creative.getWidth().equals(width)
        && creative.getHeight().equals(height)
        && type.contains(creative.getType()));
    }

    /**
     * 为广告位返回一个广告创意Creative，这里也可以返回多个
     * @param creativeObjects
     * @return
     */
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creativeObjects){
        if(CollectionUtils.isEmpty(creativeObjects)){
            return Collections.emptyList();
        }
        //todo:这个返回一个创意，也可以返回多个
        CreativeObject randomObject = creativeObjects.get(Math.abs(new Random().nextInt()) % creativeObjects.size());
        return Collections.singletonList(SearchResponse.convert(randomObject));
    }
}

