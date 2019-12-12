package com.imooc.ad.index.creativeunit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 15:43
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitObject {
    /**
     * 注意：adId=creativeId 表示一条具体的广告
     */
    private Long adId;//表示creative
    private Long unitId;
    //使用adid-unitId作为key索引
}
