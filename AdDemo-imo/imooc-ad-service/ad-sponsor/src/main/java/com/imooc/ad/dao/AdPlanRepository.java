package com.imooc.ad.dao;

import com.imooc.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:06
 * @Description:广告计划JPADAO
 */
public interface AdPlanRepository extends JpaRepository<AdPlan,Long> {

    /**
     * 根据planid和用户id查询广告计划
     * @param id
     * @param userId
     * @return
     */
    AdPlan findByIdAndUserId(Long id,Long userId);

    /**
     * 根据id集合和用户id查询
     * @param id
     * @param userId
     * @return
     */
    List<AdPlan> findAllByIdInAndUserId(List<Long> id,Long userId);

    /**
     * 根据用户id和计划名称查询
     * @param userId
     * @param planName
     * @return
     */
    AdPlan findByUserIdAndPlanName(Long userId,String planName);

    /**
     * 根据推广计划状态查询
     * @param planStatus
     * @return
     */
    List<AdPlan> findAllByPlanStatus(Integer planStatus);

}
