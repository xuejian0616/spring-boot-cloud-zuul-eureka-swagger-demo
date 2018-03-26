# spring-boot-cloud-zuul-eureka-swagger-demo

### 简介
1、使用spring-boot进行项目构建，使用spring-cloud进行服务化，使用eureka作为服务注册中心，使用zuul最为网关，并集成了swagger-UI用于API管理。

2、项目有6个工程，包含eureka服务，zuul服务，三个order服务构成的集群，payment服务。

3、payment服务不通过网关调用order服务集群。

4、eureka的注册中心管理地址：http://localhost:8080/

5、zuul的swagger接口管理地址：http://localhost:8081/swagger-ui.html 

6、项目源码的github地址：https://github.com/xuejian0616/spring-boot-cloud-zuul-eureka-swagger-demo


## 项目搭建：
### 1、创建项目结构：
![项目结构](https://upload-images.jianshu.io/upload_images/8844416-45e04f29e17248cc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
eureka-server：eureka服务注册中心，端口8080, 
zuul-server：zuul网关，端口8081 
order-server：订单系统，端口8082 
order-server1：订单系统，端口8083 
order-server2：订单系统，端口8084
payment-server：支付系统，端口8085 

### 2、eureka-server项目配置：
在服务项目的起动类上使用@EnableEurekaServer用于注解服务端，使用@EnableFeignClients注解服务的消费端。
在配置文件application.yml中添加配置：
```
##  服务端口
server:
  port: 8080
##  服务名称
spring:
  application:
    name: eureka-server
##  eureka配置
eureka:
  server:
    enable-self-preservation: false
  client:
    register-with-eureka: true
    fetch-registry: false
    service-url:
       defaultZone:   http://localhost:${server.port}/eureka/
##  日志
logging:
  level:
    root: info
    com:
      netflix:
        eureka: 'off'
        discovery:  'off'
## 关闭管理类接口的安全认证
management:
  security:
    enabled: false
```
在pom.xml中添加依赖
```
		<!-- eureka的服务端添加eureka-server -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka-server</artifactId>
		</dependency>
		<!-- eureka的消费端添加eureka -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka</artifactId>
		</dependency>
		<!--服务端的API接口需要swagger2-->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.5.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.5.0</version>
		</dependency>
```
### 3、配置zuul-server网关服务
在配置文件application.yml中添加配置：
```
# 添加路由配置
zuul:
  routes:
    payment-server:
      path: /pay/**
    order-server:
      path: /order/**
```
zuul的路由配置，zuul会将/order/**请求路由到服务名为order-server的系统上。
swagger的配置类SwaggerConfig
```
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("订单支付系统")
                .description("订单支付系统接口文档")
                .termsOfServiceUrl("http://localhost:8081")
                .contact()
                .version("1.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }
}
```
swagger文档资源配置类DocumentationConfig，用于配置接口文档的说明
```
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("订单系统", "/order/v2/api-docs", "1.0"));
        resources.add(swaggerResource("支付系统", "/pay/v2/api-docs", "1.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
```

通过在DocumentationConfig中配置文档资源，当在首页下拉框选择订单系统时，会请求http://localhost:8081/order/v2/api-docs获取文档详情

在zuul-server的启动类上添加@EnableZuulProxy和@EnableEurekaClient注解
```
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}
}
```
在pom.xml中添加依赖
```
		<!-- zuul的服务端添加zuul -->
		<dependency>
		    <groupId>org.springframework.cloud</groupId>
		    <artifactId>spring-cloud-starter-zuul</artifactId>
		</dependency>
		<!-- eureka的消费端添加eureka -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka</artifactId>
		</dependency>
		<!--服务端的API接口需要swagger2-->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.5.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.5.0</version>
		</dependency>
```
### 4、编写order-server订单服务
编写SwaggerConfig.class配置服务接口的API
```
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("cn.xuhao.demo.orderserver.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("订单系统api")
                .description("订单系统接口文档说明")
                .version("1.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list", "alpha", "schema", UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true);
    }
}
```
编写OrderServerController暴露的服务接口
```
@RestController
@Api(tags = "OrderServerController", description = "订单系统模块相关接口")
public class OrderServerController {
    @Value("${server.port}")
    String port;

    @RequestMapping("/orderSomething")
    @ApiOperation(value = "下单")
    public ResponseMessage order(@RequestParam String name) {
        String resule = "第"+name+"次请求,服务的端口为:" +port;
        return new ResponseMessageBuilder().code(200).message("OrderService1-下单成功"+resule).build();
    }
}
```
在启动类上添加 @EnableEurekaServer和@EnableFeignClients注解
其他两个order-server项目一样，就是端口号不同

### 5、编写payment-server订单服务
配置跟订单服务差不多。
添加订单服务访问接口
```
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
```
注意@FeignClient注解的name属性要跟注册中心中的服务名一致。
在支付服务中访问订单服务的接口
```
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

```



