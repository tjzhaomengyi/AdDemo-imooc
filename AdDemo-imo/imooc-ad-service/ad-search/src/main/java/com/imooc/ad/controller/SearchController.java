package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.annotation.IgnoreResponseAdvice;
import com.imooc.ad.client.SponsorClient;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 10:20
 * @Description:
 */
@Slf4j
@RestController
public class SearchController {
    private final RestTemplate restTemplate;
    private final SponsorClient sponsorClient;
    private final ISearch search;

    @Autowired
    public SearchController(RestTemplate restTemplate, SponsorClient sponsorClient, ISearch search) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
        this.search = search;
    }



    //使用Robbin获取Eureka注册的getAdPlan服务
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlanByRibbon(@RequestBody AdPlanGetRequest request){
        log.info("ad-search:getAdPlanByRibbon->{}", JSON.toJSONString(request));
        String url = "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan";
        CommonResponse response = restTemplate.postForEntity(url,request,CommonResponse.class).getBody();
        return response;
    }

    //使用feign进行获取
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request){
        log.info("ad-search:getAdPlans -> {}",JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);//如果ad-sponsor下线，可能会造成雪崩，增加断路器
    }

    /**
     * 提供广告查询的外部接口
     * @param request
     * @return
     */
    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request){
        log.info("ad-search:fetchAds -> {}",JSON.toJSONString(request));
        return search.fetchAds(request);
    }
}
