package com.imooc.ad.service;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;
/**
 * @Author: zhaomengyi
 * @Date: 2019-12-15 6:14 下午
 * @Description:Binlog增量检测，测试代码
 * @Sql: insert into ad_unit_keyword (`unit_id`,`keyword`) values (10,'标志');
 * update ad_unit_keyword set keyword='奔驰' where keyword = '标志';
 * delete from ad_unit_keyword where unit_id = 1;
 * select * from ad_unit_keyword
 */
public class BinlogServiceTest {

    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1",3306,"root","123321");
        client.registerEventListener(event -> {
            EventData data = event.getData();
            System.out.println("Event Type=" + event.getHeader().getEventType());
            if(data instanceof UpdateRowsEventData){
                System.out.println("Update----");
                System.out.println(data.toString());
            }else if(data instanceof WriteRowsEventData){
                System.out.println("Write------");
                System.out.println(data.toString());
            }
            else if(data instanceof DeleteRowsEventData){
                System.out.println("Delete----");
                System.out.println(data.toString());
            }

        });
        client.connect();
    }
}
