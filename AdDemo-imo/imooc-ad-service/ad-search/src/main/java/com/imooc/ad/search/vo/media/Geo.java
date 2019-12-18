package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 16:02
 * @Description:地理位置信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geo {
    /**
     * 经度
     */
    private Float latitude;
    /**
     * 纬度
     */
    private Float longtitude;

    private String city;
    private String province;


}
