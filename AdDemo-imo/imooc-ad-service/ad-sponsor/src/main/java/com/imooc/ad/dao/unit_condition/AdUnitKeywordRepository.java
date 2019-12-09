package com.imooc.ad.dao.unit_condition;

import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:29
 * @Description:推广单元关键词限制
 */
public interface AdUnitKeywordRepository extends JpaRepository<AdUnitKeyword,Long> {
}
