package com.imooc.ad.dao.unit_condition;

import com.imooc.ad.entity.unit_condition.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:30
 * @Description:推广单元和创意关联JPA
 */
public interface CreativeUnitRepository extends JpaRepository<CreativeUnit,Long> {
}
