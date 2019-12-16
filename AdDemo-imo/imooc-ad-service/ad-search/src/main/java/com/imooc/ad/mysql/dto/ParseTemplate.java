package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/16 10:50
 * @Description:Mysq-Binlog增量数据：template.json文件的解析器;后面的TemplateHolder的解析器会用到该解析器
 */
@Data
public class ParseTemplate {
    private String database;//数据库名称
    private Map<String,TableTemplate> tableTemplateMap = new HashMap();//String:TableName,TableTemplate：表名，层级，操作

    /**
     * 解析template.json的方法，传进来template对象
     * @param template
     * @return
     */
    public static ParseTemplate parse(Template template){
        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabase(template.getDatabase());//设置parseTemplate的database名称

        //获取每个Table的信息
        for(JsonTable table : template.getTableList()){
            String tableName = table.getTableName();//获取table名称
            Integer tableLevel = table.getLevel();//获取table层级

            TableTemplate tableTemplate = new TableTemplate();//新建一个tabletemplate对象
            tableTemplate.setTableName(tableName);//设置tableTemplate的table名称
            tableTemplate.setLevel(tableLevel.toString());//设置tableTemplate的table层级

            parseTemplate.tableTemplateMap.put(tableName,tableTemplate);//设置parseTemplate对象中tableTemplateMap对象


            //遍历操作类型对应的列
            Map<OpType, List<String>> opTypeListMap = tableTemplate.getOpTypeFieldMap();//获取tableTemplate对象中操作类型和对应列名

            //获取table中插入的列名
            for(JsonTable.Column column : table.getInsert()){
                getAndCreateIfNeed(OpType.ADD,opTypeListMap, ArrayList::new).add(column.getColumn());
            }

            //获取table中更新的列名
            for(JsonTable.Column column : table.getUpdate()){
                getAndCreateIfNeed(OpType.UPDATE,opTypeListMap,ArrayList::new).add(column.getColumn());
            }

            //获取table中删除的列名
            for(JsonTable.Column column : table.getDelete()){
                getAndCreateIfNeed(OpType.DELETE,opTypeListMap,ArrayList::new).add(column.getColumn());
            }
        }
        return parseTemplate;
    }

    private static <K,V> V getAndCreateIfNeed(K key, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(key,k -> factory.get());
    }


}
