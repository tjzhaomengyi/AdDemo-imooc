package com.imooc.ad.constant;

import lombok.Getter;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/6 17:34
 * @Description:
 */
@Getter
public enum CommonStatus {
    VALID(1,"有效状态"),
    INVALID(0,"无效状态");

    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
