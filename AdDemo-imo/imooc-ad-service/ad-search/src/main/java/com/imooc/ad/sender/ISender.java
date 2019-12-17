package com.imooc.ad.sender;

import com.imooc.ad.mysql.dto.MySqlRowData;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 15:57
 * @Description:
 */
public interface ISender {
    /**
     * 发送rowData接口
     * @param rowData
     */
    void sender(MySqlRowData rowData);
}
