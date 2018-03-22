package cn.xuhao.demo.paymentserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.service.ResponseMessage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.xuhao.demo.paymentserver.client.OrderServiceClient;
import cn.xuhao.demo.paymentserver.util.RestTemplateUtil;
import cn.xuhao.demo.paymentserver.util.Result;

/**
 * @author: xuhao
 * @Description:
 * @Date：Created on 2018/3/15 18:14.
 */
@RestController
@Api(tags = "支付系统接口", description = "支付系统模块相关接口")
public class PaymentServerController {
    @Value("${name:unknown}")
    private String name;
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderServiceClient orderServiceClient;
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 声明式的Rest客户端:Feign 调用
     * @param name
     * @return
     */
    @RequestMapping("/payForSomething/{name}")
    @ApiOperation(value = "支付接口")
    public String pay(@PathVariable String name) {
        return orderServiceClient.order(name);

    }
    /**
     * 使用 Ribbon组件和Feign调用 比较
     * @param name
     * @return
     */
    @RequestMapping("/testVoid")
    @ApiOperation(value = "支付接口")
    public String testVoid() {
    	System.out.println("调用testVoid");
    	String result = restTemplate.getForObject("http://order-server/testVoid", String.class);
    	System.out.println("result:"+result);
    	String result2 = null;
		try {
			result2 = orderServiceClient.testVoid();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result + "-----" + result2;

    }
    
    /**
     * 使用Ribbon组件负载均衡的调用服务者接口
     * @param name
     * @return
     */
    @RequestMapping("/payForSomething2/{name}")
    @ApiOperation(value = "支付接口")
    public String pay2(@PathVariable String name) {
    	String result = restTemplate.getForObject("http://order-server/orderSomething/"+name, String.class);
    	System.out.println("result:"+result);
        return result;

    }
    
    /**
     * 自己实现负载均衡的调用服务者接口--没有实现
     * @param name
     * @return
     */
    @RequestMapping("/payForSomething3/{name}")
    @ApiOperation(value = "支付接口")
    public String pay3(@PathVariable String name) {
    	Result<ResponseMessage> result =restTemplateUtil.getAsObj("order-server","/orderSomething/"+name,null,
                new ParameterizedTypeReference<Result<ResponseMessage>>(){});
    	System.out.println("result:"+result);
        return result.toString();

    }
    
    /**
     * 使用 discoveryClient 获取服务器的服务信息
     * @param name
     * @return
     */
    @RequestMapping("/test/{name}")
    @ApiOperation(value = "支付接口")
    public String test(@PathVariable String name) {
    	
    	StringBuilder buf = new StringBuilder();
        List<String> serviceIds = discoveryClient.getServices();

        System.out.println("serviceIds:"+serviceIds);
        if(!CollectionUtils.isEmpty(serviceIds)){
            for(String s : serviceIds){
                System.out.println("serviceId:" + s);
                List<ServiceInstance> serviceInstances =  discoveryClient.getInstances(s);
                if(!CollectionUtils.isEmpty(serviceInstances)){
                    for(ServiceInstance si:serviceInstances){
                        buf.append("["+si.getServiceId() +" host=" +si.getHost()+" port="+si.getPort()+" uri="+si.getUri()+" etadata= "+si.getMetadata()+"]");
                    }
                }else{
                    buf.append("no service.");
                }
            }
        }

        System.out.println("service:"+buf.toString());
        return buf.toString();

    }
    
    
}
