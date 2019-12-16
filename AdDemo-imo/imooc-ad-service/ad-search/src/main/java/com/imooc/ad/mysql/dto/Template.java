package com.imooc.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zhaomengyi
 * @Date: 2019-12-15 6:20 下午
 * @Description:对应Json整体数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    private String database;
    private List<JsonTable> tableList;
}
