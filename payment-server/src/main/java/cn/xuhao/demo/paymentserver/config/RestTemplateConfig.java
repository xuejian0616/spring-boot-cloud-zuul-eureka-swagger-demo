package cn.xuhao.demo.paymentserver.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xuhao
 * @Description: 初始化RestTemplate类，用于远程服务调用
 * @Date：Created on 2018/3/5 15:16.
 */
@Configuration
public class RestTemplateConfig {
	@LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
