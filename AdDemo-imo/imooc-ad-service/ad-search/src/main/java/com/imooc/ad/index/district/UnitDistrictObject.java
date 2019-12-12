package com.imooc.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 14:47
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {
    private Long unitId;
    private String province;
    private String city;

    //这里distinct的key:province-city
}
