package com.imooc.ad.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/17 16:40
 * @Description:对应yml配置文件中mysql的配置信息
 */
@Component
@ConfigurationProperties(prefix = "adconf.mysql")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinlogConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String binlogName;
    private Long position;
}
