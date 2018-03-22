package cn.xuhao.demo.paymentserver.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: xuhao
 * @Description:
 * @Date：Created on 2018/3/15 18:14.
 */
@Service
@FeignClient(name = "order-server", fallback = OrderServiceClient.OrderServiceClientFallback.class)
public interface OrderServiceClient {
	@RequestMapping(value = "/orderSomething/{name}",method = RequestMethod.GET)
    String order(@PathVariable(value = "name") String name);
	
	@RequestMapping("/testVoid")
	String testVoid() throws Exception;
	
    @Component
    class OrderServiceClientFallback implements OrderServiceClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceClientFallback.class);

        @Override
        public String order(@PathVariable(value = "name") String name) {
            LOGGER.info("异常发生，进入fallback方法");
            return "SERVICE B FAILED! - FALLING BACK";
        }

		@Override
		public String testVoid() throws Exception{
			LOGGER.info("异常发生，进入fallback方法");
            return "SERVICE B FAILED! - FALLING BACK";
		}
    }
}