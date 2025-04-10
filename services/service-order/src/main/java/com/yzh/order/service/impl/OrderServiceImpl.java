package com.yzh.order.service.impl;

import com.yzh.order.bean.Order;
import com.yzh.order.service.OrderService;
import com.yzh.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired //一定导入spring-cloud-starter-loadbalancer依赖
    LoadBalancerClient loadBalancerClient;
    @Override
    public Order createOrder(Long productId,Long userId){
//        Product product =  getProductFromRemote(productId);
        Product product =  getProductFromRemoteWithLoadBalancer(productId);
        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        // 远程调用
        order.setProductList(Arrays.asList(product));
        order.setAddress("北京");
        order.setNickName("张三");
        return order;

    }

    private Product getProductFromRemote(Long productId){
        //1.获取商品服务所在的所有机器ip+port
        List<ServiceInstance> instances =  discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);
        //远程url地址
        String url = "http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;
        log.info("远程调用商品服务地址："+url);
        //2.给远程发送请求
        Product product = restTemplate.getForObject(url,Product.class);
        return product;
    }

    //完成负载均衡请求
    private Product getProductFromRemoteWithLoadBalancer(Long productId){
        //1.获取商品服务所在的所有机器ip+port
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        //远程url地址
        String url = "http://"+choose.getHost()+":"+choose.getPort()+"/product/"+productId;
        log.info("远程调用商品服务地址："+url);
        //2.给远程发送请求
        Product product = restTemplate.getForObject(url,Product.class);
        return product;
    }
}
