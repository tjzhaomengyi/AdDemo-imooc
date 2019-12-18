package com.imooc.ad.search.impl;

import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 18:05
 * @Description:实现ad查询
 */
@Slf4j
@Service
public class SearchImpl implements ISearch {

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

            //根据广告位的流量类型获取初始AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            if(relation == FeatureRelation.AND){

            }
        }

        return null;
    }
}

