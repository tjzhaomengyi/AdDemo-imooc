package com.imooc.ad.mysql;

import com.imooc.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaomengyi
 * @Date: 2019-12-15 7:16 下午
 * @Description:Json文件对应tableList每项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {
    private String tableName;
    private String level;

    private Map<OpType, List<String>> opTypeListMap = new HashMap<>();

    /**
     * 字段索引->字段名
     */
    private Map<Integer,String> posMap = new HashMap<>();
}
