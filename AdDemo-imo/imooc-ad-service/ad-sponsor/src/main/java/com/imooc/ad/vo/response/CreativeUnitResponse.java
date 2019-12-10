package com.imooc.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/10 10:51
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitResponse {
    private List<Long> ids;
}
