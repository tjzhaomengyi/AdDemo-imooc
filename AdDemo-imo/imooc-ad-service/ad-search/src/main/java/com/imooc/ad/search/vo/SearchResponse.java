package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 15:48
 * @Description:广告系统响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    /**
     * key:广告编码
     *value:
     */
    private Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    /**
     * 返回的广告创意
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative{
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer heigth;
        private Integer type;
        private Integer materialType;
    }

    /**
     * 展示监测Url，媒体方展示的连接
     */
    private List<String> showMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");

    /**
     * 点击监测url，用户对广告实现点击的监测
     */
    private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");

    /**
     * 将Creative广告索引对象转换成给媒体的一个Creative对象
     * @param object
     * @return
     */
    public static Creative convert(CreativeObject object){
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeigth(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());
        return creative;
    }
}
