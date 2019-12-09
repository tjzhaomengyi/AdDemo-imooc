package com.imooc.ad.vo.request;

import com.netflix.discovery.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:46
 * @Description:AdPlan创建、更新、删除请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {

    private Long id;
    private Long userId;
    private String planName;
    private String startDate;
    private String endDate;

    public boolean createValidate() {
        return userId != null && !org.springframework.util.StringUtils.isEmpty(planName)
                && !org.springframework.util.StringUtils.isEmpty(startDate)
                && !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate() {
        return id != null && userId != null;
    }

    public boolean deleteValidate() {
        return id != null && userId != null;
    }

}
