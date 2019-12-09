package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condition.AdUnitItRepository;
import com.imooc.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.request.AdUnitDistrictRequest;
import com.imooc.ad.vo.request.AdUnitItRequest;
import com.imooc.ad.vo.request.AdUnitKeywordRequest;
import com.imooc.ad.vo.request.AdUnitRequest;
import com.imooc.ad.vo.response.AdUnitDistrictResponse;
import com.imooc.ad.vo.response.AdUnitItResponse;
import com.imooc.ad.vo.response.AdUnitKeywordResponse;
import com.imooc.ad.vo.response.AdUnitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 16:55
 * @Description:
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository adPlanRepository;
    private final AdUnitRepository adUnitRepository;

    private final AdUnitKeywordRepository adUnitKeywordRepository;
    private final AdUnitItRepository adUnitItRepository;
    private final AdUnitDistrictRepository adUnitDistrictRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository adPlanRepository, AdUnitRepository adUnitRepository, AdUnitKeywordRepository adUnitKeywordRepository, AdUnitItRepository adUnitItRepository, AdUnitDistrictRepository adUnitDistrictRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
    }

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        //查找创建这个推广单元对应的推广计划是否存在
        Optional<AdPlan> adPlan = adPlanRepository.findById(request.getPlanId());
        if(!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_ERROR);
        }

        AdUnit oldAdUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(),request.getUnitName());
        if(oldAdUnit != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newUnit = adUnitRepository.save(
          new AdUnit(request.getPlanId(),request.getUnitName(),request.getPositionType(),request.getBuget())
        );
        return new AdUnitResponse(newUnit.getId(),newUnit.getUnitName());
    }

    @Override
    @Transactional
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeyWords = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(
                    i -> unitKeyWords.add(new AdUnitKeyword(i.getUnitId(),i.getKeyword()))
            );

            ids = adUnitKeywordRepository.saveAll(unitKeyWords).stream()
                    .map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> unitIts = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(i -> unitIts.add(
                    new AdUnitIt(i.getUnitId(), i.getItTag())
            ));

           ids = adUnitItRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());

        }


        return new AdUnitItResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());

        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        List<Long> ids = Collections.emptyList();
        if(!CollectionUtils.isEmpty(request.getUnitDistricts())){
            request.getUnitDistricts().forEach(d-> unitDistricts.add(
                    new AdUnitDistrict(d.getUnitId(),d.getProvince(),d.getCity())
            ));

            ids = adUnitDistrictRepository.saveAll(unitDistricts).stream()
                    .map(AdUnitDistrict::getId).collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(ids);
    }

    //验证推广单元id是否存在,在三个限制Unit推广单元创建中使用
    private boolean isRelatedUnitExist(List<Long> unitIds){
        if(CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        return adUnitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }
}
