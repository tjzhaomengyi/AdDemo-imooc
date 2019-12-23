package com.imooc.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/23 15:50
 * @Description:HystrixDashBoard
 */
@EnableEurekaClient
@EnableHystrixDashboard
@SpringBootApplication
public class DashboardApplication {
    public static void main(String[] args){
        SpringApplication.run(DashboardApplication.class,args);
    }
}
