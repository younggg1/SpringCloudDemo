package com.yzh.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.yzh.order.bean.Order;
import com.yzh.order.feign.ProductFeignClient;
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
    @Autowired
    ProductFeignClient productFeignClient;
    @SentinelResource(value = "createOrder",blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId,Long userId){
//      普通请求 Product product =  getProductFromRemote(productId);
//      使用负载均衡请求  Product product =  getProductFromRemoteWithLoadBalancer(productId);
 //       Product product =  getProductFromRemoteWithLoadBalanceAnnotation(productId); //基于注解的负载均衡
        //使用Feign完成远程调用
        Product product = productFeignClient.getProductById(productId);
        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setAddress("尚硅谷");
        //远程查询商品列表
        order.setProductList(Arrays.asList(product));
        order.setNickName("张三");
        return order;
        //
//        try {
//            SphU.entry("hahah");
//
//        } catch (BlockException e) {
//            //编码处理
//        }

    }
    //Sentinel兜底回调
    public Order createOrderFallback(Long productId, Long userId, BlockException e){
        Order order = new Order();
        order.setId(0L);
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        order.setNickName("未知用户");
        order.setAddress("异常信息："+e.getClass());

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

    // 进阶2：完成负载均衡请求
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
    // 进阶3：基于注解式的负载均衡请求
    private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId){
        //1.获取商品服务所在的所有机器ip+port
//        ServiceInstance choose = loadBalancerClient.choose("service-product");
        //远程url地址
//        String url = "http://"+choose.getHost()+":"+choose.getPort()+"/product/"+productId;
        String url = "http://service-product/"+productId;
        //2.给远程发送请求 service-product会被动态替换
        Product product = restTemplate.getForObject(url,Product.class);
        return product;
    }
}
