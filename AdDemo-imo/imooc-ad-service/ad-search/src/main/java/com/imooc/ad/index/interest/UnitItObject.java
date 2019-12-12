package com.imooc.ad.index.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 11:56
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitItObject {
    private Long unitId;
    private String itTag;

}
