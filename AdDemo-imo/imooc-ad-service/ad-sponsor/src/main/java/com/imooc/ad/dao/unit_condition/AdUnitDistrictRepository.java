package com.imooc.ad.dao.unit_condition;

import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:28
 * @Description:地区推广单元限制jpa
 */
public interface AdUnitDistrictRepository extends JpaRepository<AdUnitDistrict,Long> {
}
