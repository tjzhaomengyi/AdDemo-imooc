package com.imooc.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/16 14:11
 * @Description:BinlogRowData数据格式
 */
@Data
public class BinlogRowData {
    private TableTemplate table;
    private EventType eventType;
    private List<Map<String,String>> before;
    private List<Map<String,String>> after;
}
