package com.imooc.ad.service.impl;

import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdPlanService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.request.AdPlanGetRequest;
import com.imooc.ad.vo.request.AdPlanRequest;
import com.imooc.ad.vo.response.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 15:37
 * @Description:
 */
@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {


    private final AdUserRepository userRepository;
    private final AdPlanRepository adPlanRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository adPlanRepository) {
        this.userRepository = userRepository;
        this.adPlanRepository = adPlanRepository;
    }

    /**
     * 创建广告推广计划
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if(!adUser.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByUserIdAndPlanName(request.getUserId(),request.getPlanName());
        if(oldPlan != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }

        AdPlan newAdPlan = adPlanRepository.save(new AdPlan(request.getUserId(),request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()),
                CommonUtils.parseStringDate(request.getEndDate())));
        return new AdPlanResponse(newAdPlan.getId(),newAdPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return adPlanRepository.findAllByIdInAndUserId(request.getIds(),request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if(!request.updateValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(),request.getUserId());
        if(plan == null){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_ERROR);
        }

        if(request.getPlanName() != null){
            plan.setPlanName(request.getPlanName());
        }

        if(request.getStartDate() != null){
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }

        if(request.getEndDate() != null){
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }

        plan.setUpdateTime(new Date());
        plan = adPlanRepository.save(plan);
        return new AdPlanResponse(plan.getId(),plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if(!request.deleteValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(),request.getUserId());
        if(plan == null){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_ERROR);
        }

        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        adPlanRepository.save(plan);
    }
}
