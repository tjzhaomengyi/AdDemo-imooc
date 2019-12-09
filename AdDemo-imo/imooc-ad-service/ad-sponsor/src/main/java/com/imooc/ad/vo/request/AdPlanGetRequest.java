package com.imooc.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 16:14
 * @Description:获取AdPlan的请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanGetRequest {
    //可以通过userId或者ids来获取
    private Long userId;
    private List<Long> ids;
    public boolean validate(){
        return userId != null && !CollectionUtils.isEmpty(ids);
    }
}
