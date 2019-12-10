package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.request.CreativeRequest;
import com.imooc.ad.vo.response.CreativeResponse;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/10 10:12
 * @Description:
 */
public interface ICreativeService {

    /**
     * 创建创意
     * @param request
     * @return
     * @throws AdException
     */
    CreativeResponse createCreative(CreativeRequest request) throws AdException;
}
