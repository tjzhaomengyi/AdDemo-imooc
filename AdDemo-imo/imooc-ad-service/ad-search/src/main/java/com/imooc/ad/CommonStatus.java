package com.imooc.ad;

import lombok.Getter;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/19 13:22
 * @Description:广告在线状态
 */
@Getter
public enum CommonStatus {
    /**
     * 在线状态
     */
    VALID(1,"有效状态"),
    INVALID(0,"无效状态");

    private Integer status;
    private String desc;

    CommonStatus(Integer status,String desc){
        this.status = status;
        this.desc = desc;
    }
}
