package w.m.vker.demo.zuulserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: xuhao
 * @Description:
 * @Date：Created on 2018/3/815 18:14.
 */
@RestController
public class ControlErrorController implements ErrorController {
    private static Logger LOGGER = LoggerFactory.getLogger(ControlErrorController.class);
    @Value("${error.path:/error}")
    private String errorPath;

    @Override
    public String getErrorPath() {
        return null;
    }

    @GetMapping(value = "${error.path:/error}", produces = "application/json")
    public @ResponseBody
    String error() {
        return "发生异常啦";
    }


}
