package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.annotation.IgnoreResponseAdvice;
import com.imooc.ad.client.SponsorClient;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
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
    @Autowired
    private final RestTemplate restTemplate;
    private final SponsorClient sponsorClient;


    public SearchController(RestTemplate restTemplate, SponsorClient sponsorClient) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
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

    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request){
        log.info("ad-search:getAdPlans -> {}",JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);//如果ad-sponsor下线，可能会造成雪崩，增加断路器
    }

}
