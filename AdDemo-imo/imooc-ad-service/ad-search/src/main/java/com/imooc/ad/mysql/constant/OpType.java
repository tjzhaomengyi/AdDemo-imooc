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

    /**
     * todo:注意下mysql5.X的事件
     * Binlog的EventType转OpType方法
     * @param eventType
     * @return
     */
    public static OpType to(EventType eventType){
        switch (eventType){
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }

    }
}
