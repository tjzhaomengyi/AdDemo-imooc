package com.imooc.ad.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zhaomengyi
 * @Date: 2019-12-15 6:14 下午
 * @Description:对应Json中表的表示，tablelist中每个元素
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonTable {
    private String tableName;
    private Integer level;

    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column{
        private String column;
    }

}
