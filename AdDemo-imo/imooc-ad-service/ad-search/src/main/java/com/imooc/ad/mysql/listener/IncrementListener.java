package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 15:59
 * @Description:增量数据监听器
 */
@Slf4j
@Component
public class IncrementListener implements IListener{

    @Resource(name = "indexSender")
    private ISender sender;

    @Autowired
    private final AggregationListener aggregationListener;

    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    /**
     * 需要在bean加载后注册每个表对应的增量监听器
     */
    @PostConstruct
    @Override
    public void register() {
        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((k,v) -> aggregationListener.register(v,k,this));
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        //包装成最后投递的数据
        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        //取出eventData模板中该操作字段对应的列表，判断操作列是否为空，如果为空直接返回
        List<String> fieldList = table.getOpTypeFieldMap().get(opType);
        if(null == fieldList){
            log.warn("{} not support for {}",opType,table.getTableName());
            return;
        }

        //遍历EventData中的数据，获取对应的列名和数值，塞入rowData的FieldValueMap中
        //todo:这里获取FieldValueMap的list，直接往里塞不就完了？
//        List<Map<String,String>> afterMapList = eventData.getAfter();
//        rowData.getFieldValueMap().addAll(afterMapList);
        for (Map<String,String> afterMap:eventData.getAfter()){
            Map<String,String> colValueMap = new HashMap<>();
            for(Map.Entry<String,String> entry : afterMap.entrySet()){
                String colName = entry.getKey();
                String colValue = entry.getValue();
                colValueMap.put(colName,colValue);
            }
            rowData.getFieldValueMap().add(colValueMap);
        }
        sender.sender(rowData);
    }
}
