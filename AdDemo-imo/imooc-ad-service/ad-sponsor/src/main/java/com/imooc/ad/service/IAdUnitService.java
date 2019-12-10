package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.request.*;
import com.imooc.ad.vo.response.*;

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

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;



}

