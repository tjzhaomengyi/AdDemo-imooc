package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.entity.Creative;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.ICreativeService;
import com.imooc.ad.vo.request.CreativeRequest;
import com.imooc.ad.vo.response.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/10 10:22
 * @Description:
 */
@Service
public class CreativeServiceImpl implements ICreativeService {

    @Autowired
    private final CreativeRepository creativeRepository;

    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }


    @Override
    @Transactional
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Creative creative = creativeRepository.save(request.convertToEntity());
        return new CreativeResponse(creative.getId(),creative.getName());
    }
}
