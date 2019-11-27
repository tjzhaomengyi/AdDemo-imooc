package com.imooc.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/11/27 18:16
 * @Description:Zuul启动类
 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGateWayApplication.class,args);
    }
}
