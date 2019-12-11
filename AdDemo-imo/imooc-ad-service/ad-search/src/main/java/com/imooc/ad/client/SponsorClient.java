package com.imooc.ad.client;

import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 14:36
 * @Description:
 */

@FeignClient(value="eureka-client-ad-sponsor",fallback = SponsorClientHystrix.class) //value指向调用微服务的名称；fallback表示这个client对应的熔断服务
public interface SponsorClient {

    /**
     * 查询广告计划集合
     * @param request
     * @return
     */
    @RequestMapping(value = "/ad-sponsor/get/adPlan",method = RequestMethod.POST)//指定调用的服务接口
    CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request);
}
