package com.imooc.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 16:42
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {
    private Long planId;
    private String unitName;
    private Integer positionType;
    private Long buget;

    public boolean createValidate(){
        return null != planId && !StringUtils.isEmpty(unitName)
                && positionType != null && buget != null;
    }
}
