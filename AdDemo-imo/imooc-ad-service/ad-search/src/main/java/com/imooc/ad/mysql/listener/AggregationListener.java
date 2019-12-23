package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/16 15:04
 * @Description:手动聚合Mysql的Binlog日志
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener{
    private String dbName;
    private String tableName;

    private Map<String,IListener> listenerMap = new HashMap<>();//每张数据表对应的监听器

    private final TemplateHolder templateHolder;//模板信息

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    /**
     * 生成监听器的key--dbName:tableName
     * @param dbName
     * @param tableName
     * @return
     */
    private String genKey(String dbName,String tableName){
        return dbName + ":" + tableName;
    }

    /**
     * 对监听器进行注册
     * @param dbName
     * @param tableName
     * @param listener
     */
    public void register(String dbName,String tableName,IListener listener){
        log.info("register : {}-{}",dbName,tableName);
        this.listenerMap.put(genKey(dbName,tableName),listener);//注册监听器
    }

    /**
     * 对mysql中原始的binlog的event事件进行监听
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type:{}",type);
        //EventType的TABLE_MAP类型中包括tableName和dbname信息
        if(type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();//event的data包换binlog的数据
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        //如果不是增删改类型type不处理
        if(type != EventType.EXT_UPDATE_ROWS
                && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS){
            return;
        }

        //表明和库名是否都填充
        if(StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            log.error("no meta data event");
        }

        //找出在map中存在的监听器
        String key = genKey(this.dbName,this.tableName);
        IListener listener = this.listenerMap.get(key);
        //如果查找的Listener为空，表示这个表不关心，不被监听
        if(null == listener){
            log.debug("skip {}",key);
            return;
        }

        //处理需要监听的binlog
        log.info("trigger event: {}",type.name());
        try {
            //将EventData转成自定义的BinlogRowData，重要，这个是增量数据转换的关键。
            BinlogRowData rowData = buildRowData(event.getData());
            if (null == rowData) {
                return;
            }

            rowData.setEventType(type);//设置自定义BinlogRowData的EventType类型
            listener.onEvent(rowData);//自定义Listener处理rowdata数据
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }finally {
            //处理完成后需要将dbname和dbtable清空，好处理下一个event事件
            this.dbName="";
            this.tableName="";
        }



    }

    /**
     * 功能：根据EventData获取Binlog每条信息，拿去其中的rows字段
     * 数据格式：
     * Write------
     * WriteRowsEventData{tableId=113, includedColumns={0, 1, 2}, rows=[
     *     [1, 10, 标志]
     * ]}
     * Update----
     * UpdateRowsEventData{tableId=113, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
     *     {before=[1, 10, 标志], after=[1, 10, 奔驰]}
     * ]}
     * @param eventData
     * @return
     */
    private List<Serializable[]> getAfterValues(EventData eventData){
        //拿到的数据[1, 10, 标志]
        if(eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData) eventData).getRows();
        }
        //拿到的数据{before=[1, 10, 标志], after=[1, 10, 奔驰]
        if(eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }
        //拿到的数据[1, 10, 标志]
        if(eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return null;

    }

    /**
     * 将原始的Binlog的EventType事件转换为我们自己定义BinlogRowData可解析类型
     * @param eventData
     * @return
     */
    private BinlogRowData buildRowData(EventData eventData){
        TableTemplate table = templateHolder.getTable(tableName);
        if(null == table){
            log.warn("table {} not found",tableName);
            return null;
        }

        List<Map<String,String>> afterMapList = new ArrayList<>();
        for(Serializable[] after : getAfterValues(eventData)){//获取eventData中每个Rows数据，然后遍历
            //after表示rows元素比如：[1, 10, 宝马]
            Map<String,String> afterMap = new HashMap<>();
            int colLen = after.length;
            for(int i=0; i<colLen;i++){
                //取出row当前位置对应的列名
                String colName = table.getPosMap().get(i);
                //如果这个列不关心
                if(null == colName){
                    log.debug("ignore position:{}",i);
                    continue;
                }
                String colValue = after[i].toString();
                afterMap.put(colName,colValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData binlogRowData = new BinlogRowData();
        binlogRowData.setAfter(afterMapList);
        binlogRowData.setTable(table);
        return  binlogRowData;
    }
}
