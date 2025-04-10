package com.yzh.product;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
public class ProductApplicationTest {
    @Autowired
    DiscoveryClient discoveryClient;


    @Test
    public void discoveryClientTest(){
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
            //获取ip+port
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println("instance.getHost() = " + instance.getHost());
                System.out.println("instance.getPort() = " + instance.getPort());
            }
        }
    }
    @Autowired
    NacosServiceDiscovery nacosServiceDiscovery;//二者效果一样，这个依赖nacos
    @Test
    public void nacosDiscoveryClientTest() throws NacosException {
        List<String> services = nacosServiceDiscovery.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
            List<ServiceInstance> instances = nacosServiceDiscovery.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println("instance.getHost() = " + instance.getHost());
                System.out.println("instance.getPort() = " + instance.getPort());
            }
        }
    }
}