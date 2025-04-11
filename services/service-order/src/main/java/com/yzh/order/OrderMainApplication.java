package com.yzh.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class,args);
    }
    //1.项目启动就监听配置文件变化
    //2.发生变化后拿变化值
    //发送邮件
    @Bean
    ApplicationRunner applicationRunner (NacosConfigManager nacosConfigManager){
        return args -> {
            nacosConfigManager.getConfigService().addListener("order.properties", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("变化的配置信息"+configInfo);
                    System.out.println("邮件通知。。。");
                }
            });
            System.out.println("项目启动就监听配置文件变化");
        };
    }

}
