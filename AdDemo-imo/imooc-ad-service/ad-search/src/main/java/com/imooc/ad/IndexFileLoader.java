package com.imooc.ad;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/13 16:58
 * @Description:解析数据文件，加载索引到内存中。
 * 全量数据索引加载放生在检索系统启动后
 */
@Component
@DependsOn("dataTable")//需要依赖DataTable组件才能实现索引文件的挤在
public class IndexFileLoader {

    /**
     * 初始化：bean初始化时就执行，当IndexFileLoader加载到Spring容器中init方法就要执行，增加PostConstr注解
     * PostConstruct注解的方法会在IndexFileLoader依赖注入完成后自动调用
     */
    @PostConstruct
    public void init(){
        initDir(DConstant.DATA_ROOT_DIR);

        //注意顺序，要按照2到4的层级关系顺序进行加载

        //2级别
        List<String> adPlanStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_PLAN));
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdPlanTable.class), OpType.ADD
        ));

        List<String> adCreativeStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE));
        adCreativeStrings.forEach(p -> AdLevelDataHandler.handlerLevel2(
                JSON.parseObject(p, AdCreativeTable.class),
                OpType.ADD
        ));

        //3级别
        List<String> adUnitStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT));
        adUnitStrings.forEach(u -> AdLevelDataHandler.hanleLevel3(
                JSON.parseObject(u, AdUnitTable.class),
                OpType.ADD
        ));

        List<String> adCreativeUnitStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE_UNIT));
        adCreativeUnitStrings.forEach( cu -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(cu, AdCreativeUnitTable.class),
                OpType.ADD
        ));

        //4级别
        List<String> adUnitDistrictStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_DISTRICT));
        adUnitDistrictStrings.forEach(d -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(d, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        List<String> adUnitItStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_IT));
        adUnitItStrings.forEach(i -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(i,AdUnitItTable.class),
                OpType.ADD
        ));

        List<String> adUnitKeywordStrings = loadDumpData(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_KEYWORD));
        adUnitKeywordStrings.forEach(k -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(k,AdUnitKeywordTable.class),
                OpType.ADD
        ));
    }

    /**
     * 初始化文件夹
     * @param dataRootDir
     */
    private void initDir(String dataRootDir){
        File dir = new File(dataRootDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }
    /**
     * 将索引文件读取
     * @param fileName
     * @return
     */
    private List<String> loadDumpData(String fileName){
        try{
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName));
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
