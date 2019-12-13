package com.imooc.ad.index;

import org.aspectj.weaver.IClassFileProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/12 17:18
 * @Description:通过DataTable可以获取所有的index索引对象服务
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {
    private static ApplicationContext applicationContext;

    private static final Map<Class,Object> dataTableMap = new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    /**
     * 初始化顺序，DataTable需要最先初始化，定义一个最小的值，让它初始化的优先级最高
     * @return
     */
    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    /**
     * 通过Class获取对应的index服务对象
     * DataTable.of(AdPlanIndex.class),AdPlanIndex为最终转成的index索引对象类型
     */

    public static <T> T of(Class<T> clazz){
        T instance = (T) dataTableMap.get(clazz);
        if(null != instance){
            return instance;
        }
        dataTableMap.put(clazz,bean(clazz));
        return (T)dataTableMap.get(clazz);
    }

    private static <T> T bean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    private static <T> T bean(Class clazz){
        return (T)applicationContext.getBean(clazz);
    }
}
