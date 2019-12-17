package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 14:22
 * @Description:进行mysql增量信息投递增加的数据类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySqlRowData {
    private String tableName;
    private String level;
    private OpType opType;//根据EventType转换为对应的OpType
    private List<Map<String,String>> fieldValueMap = new ArrayList<>();//增加列和值
}
