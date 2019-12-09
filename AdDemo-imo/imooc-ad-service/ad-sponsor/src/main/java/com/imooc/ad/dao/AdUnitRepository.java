package com.imooc.ad.dao;

import com.imooc.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:17
 * @Description:广告单元JPADAO接口
 */

public interface AdUnitRepository extends JpaRepository<AdUnit,Long> {

    /**
     * 根据广告计划和推广单元名称查询推广单元
     * @param planId
     * @param unitName
     * @return
     */
    AdUnit findByPlanIdAndUnitName(Long planId,String unitName);

    /**
     * 根据推广单元状态查找推广单元
     * @param unitStatus
     * @return
     */
    List<AdUnit> findAllByUnitStatus(Integer unitStatus);

    /**
     * 根据广告计划id查询对应推广单元
     * @param planId
     * @return
     */
    List<AdUnit> findByPlanId(Long planId);
}
