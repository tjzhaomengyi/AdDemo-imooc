package com.imooc.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.table.*;
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
        } else if(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME.equals(rowData.getTableName())){
            List<AdCreativeTable> creativeTables = new ArrayList<>();
            for(Map<String,String> fieldValueMap:rowData.getFieldValueMap()){
                AdCreativeTable creativeTable = new AdCreativeTable();

                fieldValueMap.forEach((colName,colValue) ->{
                    switch (colName){
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(colValue));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(colValue);
                            break;
                        default:
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            }
            creativeTables.forEach(c -> AdLevelDataHandler.handlerLevel2(c,rowData.getOpType()));
        }
    }

    private void level3RowData(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdUnitTable> unitTables = new ArrayList<>();
            for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                AdUnitTable unitTable = new AdUnitTable();
                fieldValueMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                        default:
                            break;
                    }
                });
                unitTables.add(unitTable);
            }
            unitTables.forEach(u -> AdLevelDataHandler.hanleLevel3(u,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();

                fieldValueMap.forEach((k,v) ->{
                    switch (k){
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                        default:
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }
            creativeUnitTables.forEach(u -> AdLevelDataHandler.handleLevel3(u,rowData.getOpType()));
        }
    }

    private void level4RowData(MySqlRowData rowData){
        switch (rowData.getTableName()){
            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();
                    fieldValueMap.forEach((k,v)->{
                        switch (k){
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                            default:
                                break;
                        }
                    });
                    districtTables.add(districtTable);
                }
                districtTables.forEach(d -> AdLevelDataHandler.handleLevel4(d,rowData.getOpType()));
                break;

            case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                List<AdUnitItTable> itTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                    AdUnitItTable itTable = new AdUnitItTable();
                    fieldValueMap.forEach((k,v) ->{
                        switch (k){
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                itTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                itTable.setItTag(v);
                            default:
                                break;
                        }
                    });
                    itTables.add(itTable);
                }
                itTables.forEach(i -> AdLevelDataHandler.handleLevel4(i,rowData.getOpType()));
                break;

            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:
                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();
                    fieldValueMap.forEach((k,v) ->{
                        switch (k){
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                            default:
                                break;
                        }
                    });
                    keywordTables.add(keywordTable);
                }
                keywordTables.forEach(k -> AdLevelDataHandler.handleLevel4(k,rowData.getOpType()));
                break;
            default:
                break;
        }
    }
}