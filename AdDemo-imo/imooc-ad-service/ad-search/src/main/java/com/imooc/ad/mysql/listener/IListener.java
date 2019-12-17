package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.dto.BinlogRowData;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/16 14:13
 * @Description:实现增量索引监听的接口
 */
public interface IListener {

    void register();

    void onEvent(BinlogRowData eventData);//实现增量数据的索引更新，参数是已经转换的BinlogRowData
}


