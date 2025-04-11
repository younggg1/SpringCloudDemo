package com.yzh.order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="order")//配置批量绑定在nacaos下无需@RefreshScope就可自动无感刷新
public class OrderProperties {

    String timeout;

    String autoConfirm;
}
