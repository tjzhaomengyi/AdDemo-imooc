package com.imooc.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.table.AdPlanTable;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.index.DataLevel;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 18:39
 * @Description:
 */
@Slf4j
@Component("indexSender")
public class IndexSender implements ISender {
    /**
     * 先解析属于哪个层级，然后进行增量数据的投递
     * @param rowData
     */
    @Override
    public void sender(MySqlRowData rowData) {
        String level = rowData.getLevel();
        if(DataLevel.LEVEL2.getLevel().equals(level)){

        }else if(DataLevel.LEVEL3.getLevel().equals(level)){

        }else if(DataLevel.LEVEL4.getLevel().equals(level)){

        }else{
            log.error("MySqlRowData error: {}", JSON.toJSONString(rowData));
        }
    }

    /**
     * 将rowData数据转成index增量数据
     * @param rowData
     */
    private void level2RowData(MySqlRowData rowData){
        //如果是ad_plan的增量数据
        if(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME.equals(rowData.getTableName())){
            List<AdPlanTable> planTableList = new ArrayList<>();
            for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                AdPlanTable planTable = new AdPlanTable();
                fieldValueMap.forEach((colName,colValue) ->{
                    switch (colName){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(colValue));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(colValue));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.pareseStringDate(colValue));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.pareseStringDate(colValue));
                            break;
                        default:
                            break;
                    }
                });
                planTableList.add(planTable);
            }
            planTableList.forEach(p ->
                    AdLevelDataHandler.handleLevel2(p,rowData.getOpType()));
        }
    }
}
