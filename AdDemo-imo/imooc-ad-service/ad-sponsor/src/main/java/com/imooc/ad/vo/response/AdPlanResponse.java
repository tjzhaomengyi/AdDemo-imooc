package com.imooc.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:55
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanResponse {
    /**
     * 这里只用id和planname作为Response的返回响应
     */
    private Long id;
    private String planName;
}
