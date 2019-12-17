package com.imooc.ad.index;

import lombok.Getter;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 17:38
 * @Description:定义索引层级Level表示类
 */
@Getter
public enum  DataLevel {
    LEVEL2("2","level 2"),
    LEVEL3("3","level 3"),
    LEVEL4("4","level 4");
    private String level;
    private String desc;

    DataLevel(String level,String desc){
        this.level = level;
        this.desc = desc;
    }
}
