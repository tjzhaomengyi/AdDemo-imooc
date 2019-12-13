package com.imooc.ad.mysql.constant;


import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/13 14:10
 * @Description:
 */
public enum OpType {
    /**
     * mysql操作类型
     */
    ADD,
    UPDATE,
    DELETE,
    OTHER;

    //
//    public static OpType to(EventType eventType){
//
//    }
}
