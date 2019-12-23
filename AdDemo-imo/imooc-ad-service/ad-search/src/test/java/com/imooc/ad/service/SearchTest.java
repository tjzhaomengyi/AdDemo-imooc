package com.imooc.ad.service;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.Application;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import com.imooc.ad.search.vo.media.App;
import com.imooc.ad.search.vo.media.Device;
import com.imooc.ad.search.vo.media.Geo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/23 17:17
 * @Description: 搜索测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SearchTest {

    @Autowired
    private ISearch search;

    @Test
    public void testSearch(){
        //新建searchRequest并构建
        SearchRequest request = new SearchRequest();

        //第一个测试条件
        AdSlot adSlot = new AdSlot("ad-x",1,1080,720, Arrays.asList(1,2),1000);
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "aaa",
                Collections.singletonList(adSlot),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));

        request.setFeatureInfo(buildExampleFeatureInfo(Arrays.asList("宝马","大众"),
                Collections.singletonList(new DistrictFeature.ProvinceAndCity("安徽省","合肥市")),
                Arrays.asList("台球","游泳"),
                FeatureRelation.OR));
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(search.fetchAds(request)));


    }

    /**
     * 构建app信息，可以随意，不作为匹配信息
     * @return
     */
    private App buildExampleApp(){
        return new App("imooc","imooc","com.imooc","video");
    }

    /**
     * 构建地理信息
     * @return
     */
    private Geo buildExampleGeo(){
        return new Geo((float)100.28,(float)88.61,"北京市","北京市");
    }

    /**
     * 构建设备信息
     * @return
     */
    private Device buildExampleDevice(){
        return new Device("iphone","0xxx","127.0.0.1","x","1080 720","1080 720","1234");
    }

    /**
     * 构建特征信息
     * @param keywords
     * @param provinceAndCities
     * @param its
     * @param relation
     * @return
     */
    private SearchRequest.FeatureInfo buildExampleFeatureInfo(List<String> keywords, List<DistrictFeature.ProvinceAndCity> provinceAndCities,
                                                              List<String> its, FeatureRelation relation){
        return new SearchRequest.FeatureInfo(new KeywordFeature(keywords),new DistrictFeature(provinceAndCities),
                new ItFeature(its),relation);
    }

}
