package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 15:55
 * @Description:app信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class App {
    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用包名
     */
    private String pkgName;

    /**
     * activity名称
     */
    private String activityName;
}
