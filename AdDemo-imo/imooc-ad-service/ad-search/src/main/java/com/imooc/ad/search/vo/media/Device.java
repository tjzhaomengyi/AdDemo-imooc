package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 15:58
 * @Description:设备信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    /**
     * 设备id
     */
    private String deviceCode;

    /**
     * mac地址
     */
    private String mac;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 机型编码
     */
    private String model;

    /**
     * 分辨率尺寸
     */
    private String diplaySize;

    /**
     * 屏幕尺寸
     */
    private String screenSize;

    /**
     * 设备序列号
     */
    private String serialName;
}
