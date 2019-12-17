package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.imooc.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 16:42
 * @Description:Binlog监听器客户端
 */
@Slf4j
@Component
public class BinlogClient {

    private static final Long DEFAULT_POSITION = -1L;

    private BinaryLogClient client;

    @Autowired
    private final BinlogConfig config;
    private final AggregationListener listener;
    private final Executor binlogClientConnectExecutor;

    public BinlogClient(BinlogConfig config, AggregationListener listener, Executor binlogClientConnectExecutor) {
        this.config = config;
        this.listener = listener;
        this.binlogClientConnectExecutor = binlogClientConnectExecutor;
    }


    public void connect(){
        binlogClientConnectExecutor.execute(()->{
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
            //校验
            if(!StringUtils.isEmpty(config.getBinlogName()) && !DEFAULT_POSITION.equals(config.getPosition())){
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);

            try {
                log.info("connecting to mysql start");
                client.connect();
                log.info("connecting to mysql end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void close(){
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
