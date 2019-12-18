package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 16:14
 * @Description:区域匹配特征
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictFeature {
    private List<ProvinceAndCity> districts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceAndCity{
        private String province;
        private String city;
    }
}
