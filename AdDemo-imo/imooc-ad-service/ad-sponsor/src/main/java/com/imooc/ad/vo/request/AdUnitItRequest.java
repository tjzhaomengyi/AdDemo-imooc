package com.imooc.ad.vo.request;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 16:48
 * @Description:兴趣爱好限制请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItRequest {
    private List<UnitIt> unitIts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitIt {
        private Long unitId;
        private String itTag;
    }
}
