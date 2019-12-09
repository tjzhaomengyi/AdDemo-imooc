package com.imooc.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 17:29
 * @Description:爱好限制推广单元响应结果，是一个List集合
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItResponse {
    private List<Long> ids;
}
