package w.m.vker.demo.paymentserver.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuhao
 * @Description:
 * @Date：Created on 2018/3/815 18:14.
 */
@Service
@FeignClient(name = "ORDER-SERVER", fallback = OrderServiceClient.OrderServiceClientFallback.class)
public interface OrderServiceClient {
    @RequestMapping(value = "/orderSomething",method = RequestMethod.GET)
    String order(@RequestParam(value = "name") String name);

    @Component
    class OrderServiceClientFallback implements OrderServiceClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceClientFallback.class);

        @Override
        public String order(@RequestParam(value = "name") String name) {
            LOGGER.info("异常发生，进入fallback方法");
            return "SERVICE B FAILED! - FALLING BACK";
        }
    }
}