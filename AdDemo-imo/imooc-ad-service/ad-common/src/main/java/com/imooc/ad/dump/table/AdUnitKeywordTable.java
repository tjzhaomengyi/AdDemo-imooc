package com.imooc.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 18:26
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitKeywordTable {
    private Long unitId;
    private String keyword;
}
