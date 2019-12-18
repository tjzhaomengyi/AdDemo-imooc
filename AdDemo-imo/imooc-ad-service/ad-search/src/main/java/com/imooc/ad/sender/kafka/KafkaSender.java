package com.imooc.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 11:39
 * @Description:将增量数据投递到kafka中
 */
@Slf4j
@Component("kafkaSender")
public class KafkaSender implements ISender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

    /**
     * kafka消息监听，指定不同group每个group会得到同样的消息
     * @param record
     */
    @KafkaListener(topics = {"${adconf.kafka.topic}"},groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            //反序列化为MysqlRowData
            MySqlRowData rowData = JSON.parseObject(message.toString(),MySqlRowData.class);
            System.out.println("kafka processMsyqlRowData" + JSON.toJSONString(rowData));
        }
    }
}
