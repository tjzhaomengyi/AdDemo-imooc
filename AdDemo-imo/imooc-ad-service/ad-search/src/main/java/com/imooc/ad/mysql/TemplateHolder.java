package com.imooc.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/16 11:42
 * @Description:1、解析json文件，2、完成binlog索引和列名的映射
 */
@Slf4j
@Component
public class TemplateHolder {
    private ParseTemplate parseTemplate;//template.json解析器
    private final JdbcTemplate jdbcTemplate;//进行mysql元数据查询的jdbc

    //查询数据库中表索引和列名的对应关系
    private String SQL_SCHEMA = "select info.TABLE_SCHEMA,info.TABLE_NAME,info.COLUMN_NAME,info.ORDINAL_POSITION" +
            "from information_schema.`COLUMNS` info" +
            "where info.TABLE_SCHEMA = ? and info.TABLE_NAME = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Spring容器加载TeamplateHolder后自动执行初始化，得到Json解析
     */
    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    /**
     * 通过tableName获取TableTemplate对象
     * @param tableName
     * @return
     */
    public TableTemplate getTable(String tableName){
        return parseTemplate.getTableTemplateMap().get(tableName);
    }
    /**
     * 加载template.json文件
     * @param path
     */
    private void loadJson(String path){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();//获取当前线程的ClassLoader
        InputStream inputStream = classLoader.getResourceAsStream(path);
        try {
            Template template = JSON.parseObject(inputStream, Charset.defaultCharset(),Template.class);
            this.parseTemplate = ParseTemplate.parse(template);//解析Template内容，得到ParseTemplate类给出的解析结果
            loadMeta();//加载binlog索引和列名映射，写到parseTe的属性中
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse json file");
        }
    }

    /***
     * 获取mysql元数据信息，索引号和列名映射
     */
    private void loadMeta(){
        this.parseTemplate.getTableTemplateMap().forEach((tableName,tableTemplate) -> {
            List<String> insertFields = tableTemplate.getOpTypeFieldMap().get(OpType.ADD);
            List<String> updateFields = tableTemplate.getOpTypeFieldMap().get(OpType.UPDATE);
            List<String> deleteFields = tableTemplate.getOpTypeFieldMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA,new Object[]{this.parseTemplate.getDatabase(),tableName},(rs,i)->{
                int pos = rs.getInt("ORDINAL_POSITION");//列索引
                String columnName = rs.getString("COLUMN_NAME");
                if(null != updateFields && updateFields.contains(columnName)
                || null != insertFields && insertFields.contains(columnName)
                || null != deleteFields && deleteFields.contains(columnName)){
                    tableTemplate.getPosMap().put(pos-1,columnName); //填充tableTemplate中posMap的索引和列名映射
                }
                return null;
            });
        });
    }
}
