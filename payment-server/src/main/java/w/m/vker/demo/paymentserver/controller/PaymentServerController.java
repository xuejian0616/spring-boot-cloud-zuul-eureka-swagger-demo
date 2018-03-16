package w.m.vker.demo.paymentserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import w.m.vker.demo.paymentserver.client.OrderServiceClient;

/**
 * @author: xuhao
 * @Description:
 * @Date：Created on 2018/3/815 18:14.
 */
@RestController
@Api(tags = "支付系统接口", description = "支付系统模块相关接口")
public class PaymentServerController {
    @Value("${name:unknown}")
    private String name;

    @Autowired
    private OrderServiceClient orderServiceClient;


    @RequestMapping("/payForSomething")
    @ApiOperation(value = "支付接口")
    public String pay() {
        return orderServiceClient.order("123");

    }
}
