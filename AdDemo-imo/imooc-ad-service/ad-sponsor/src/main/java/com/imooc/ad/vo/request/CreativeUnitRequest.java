package com.imooc.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/10 10:48
 * @Description:创建推广单元和创意关联的请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitRequest {

    private List<CreateUnitItem> unitItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUnitItem{
        private Long creativeId;
        private Long unitId;
    }

}
