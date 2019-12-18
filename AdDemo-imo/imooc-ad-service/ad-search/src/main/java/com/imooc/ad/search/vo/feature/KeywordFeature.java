package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 16:12
 * @Description:keyword匹配特征
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordFeature {
    private List<String> keywords;
}
