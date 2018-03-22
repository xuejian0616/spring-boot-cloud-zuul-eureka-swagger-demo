package cn.xuhao.demo.paymentserver.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
@Lazy(false)
public class RestTemplateUtil {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    
    private static Integer pos=0;
    private String serverName;
    
    public RestTemplateUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
//        restTemplate.setMessageConverters(Arrays.asList(new GsonHttpMessageConverter()));
    }

    /**
     * 支持泛型的List封装
     * @param method
     * @param url
     * @param params
     * @param responseType
     * @param <T>
     * @return
     */
    private  <T> Result<List<T>> exchangeAsList(HttpMethod method, String url, Map<String,Object> params, ParameterizedTypeReference<Result<List<T>>> responseType) {
        HttpEntity requestEntity = new HttpEntity(params);
        return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
    }

    /**
     * 支持泛型的List封装 POST
     * @param url
     * @param body
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<List<T>> postAsList(String url, Object body, ParameterizedTypeReference<Result<List<T>>> responseType) {
        HttpEntity requestEntity = new HttpEntity(body);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
    }

    /**
     * 支持泛型的封装 POST
     * @param url
     * @param body
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<T> postAsT(String url, Object body, ParameterizedTypeReference<Result<T>> responseType) {
        HttpEntity requestEntity = new HttpEntity(body);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
    }

    /**
     * 支持泛型的List封装 GET
     * @param url
     * @param params
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<List<T>> getAsList(String url, Map<String,Object> params, ParameterizedTypeReference<Result<List<T>>> responseType) {
        HttpEntity requestEntity = new HttpEntity(null);
        return restTemplate.exchange(url+getUrlParamsByMap(params), HttpMethod.GET, requestEntity, responseType).getBody();
    }

    /**
     * 支持泛型的封装 GET
     * @param url
     * @param params
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<T> getAsT(String url, Map<String,Object> params, ParameterizedTypeReference<Result<T>> responseType) {
        HttpEntity requestEntity = new HttpEntity(null);
        return restTemplate.exchange(url+getUrlParamsByMap(params), HttpMethod.GET, requestEntity, responseType).getBody();
    }
    /**
     * 支持泛型的Object封装 Object不能再次内套泛型
     * @param method
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    private  <T> Result<T> exchangeAsObj(HttpMethod method, String url, Object object, ParameterizedTypeReference<Result<T>> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(object);
        return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
    }

    /**
     * 支持泛型的Object封装  POST请求Object不能再次内套泛型
     * @param url
     * @param params
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<T> postAsObj( String url, Object params, ParameterizedTypeReference<Result<T>> responseType) {
        return this.exchangeAsObj(HttpMethod.POST,url,params, responseType);
    }

    /**
     * 支持泛型的Object封装 Get请求Object不能再次内套泛型
     * @param url
     * @param params
     * @param responseType
     * @param <T>
     * @return
     */
    public  <T> Result<T> getAsObj( String serverName,String RequestMapping, Map<String,Object> params, ParameterizedTypeReference<Result<T>> responseType) {
    	String serverIP = getServerUrlByServerName(serverName);
    	String url = serverIP + RequestMapping;
    	System.out.println("请求的url："+url);
//    	System.out.println("请求restTemplate："+restTemplate.getForObject(url, String.class));
    	return this.exchangeAsObj(HttpMethod.GET,url+getUrlParamsByMap(params),null, responseType);
    }
    public  <T> Result<T> getForObject( String url, Map<String,Object> params, ParameterizedTypeReference<Result<T>> responseType) {
    	return this.exchangeAsObj(HttpMethod.GET,url+getUrlParamsByMap(params),null, responseType);
    }
    
    private static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }
    
    private String getServerUrlByServerName(String serverName){
    	List<String> serviceIdList = new ArrayList<String>();
    	List<String> serviceIds = discoveryClient.getServices();
    	String erverUrl="";

        System.out.println("serviceIds:"+serviceIds);
        if(!CollectionUtils.isEmpty(serviceIds)){
            for(String s : serviceIds){
                System.out.println("s:"+s);
            	if(serverName.equals(s)) {
                    List<ServiceInstance> serviceInstances =  discoveryClient.getInstances(s);
                    if(!CollectionUtils.isEmpty(serviceInstances)){
                        for(ServiceInstance si:serviceInstances){
//                            "["+si.getServiceId() +" host=" +si.getHost()+" port="+si.getPort()+" uri="+si.getUri()+"]");
                            serviceIdList.add(si.getUri()+"");
                        }
                    }else{
                        System.out.println("没有"+serverName+"服务!");
                    }
            	}
            }
        }
        if(serviceIdList.size() >0 && serviceIdList != null) {
            //轮训调取id
            synchronized (pos){
                if(pos >= serviceIdList.size())
                    pos=0;
                erverUrl = serviceIdList.get(pos);
                pos++;
            }
        }
        
        return erverUrl;
    }
}
