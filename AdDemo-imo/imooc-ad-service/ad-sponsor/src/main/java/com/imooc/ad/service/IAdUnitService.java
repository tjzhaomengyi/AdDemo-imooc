package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.request.AdUnitDistrictRequest;
import com.imooc.ad.vo.request.AdUnitItRequest;
import com.imooc.ad.vo.request.AdUnitKeywordRequest;
import com.imooc.ad.vo.request.AdUnitRequest;
import com.imooc.ad.vo.response.AdUnitDistrictResponse;
import com.imooc.ad.vo.response.AdUnitItResponse;
import com.imooc.ad.vo.response.AdUnitKeywordResponse;
import com.imooc.ad.vo.response.AdUnitResponse;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 16:46
 * @Description:
 */
public interface IAdUnitService {
    /**
     * 创建一个推广单元
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    /**
     * 创建区域限制的推广单元
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;



}

