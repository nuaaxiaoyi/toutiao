package com.nowcoder.toutiao.controller;


import com.nowcoder.toutiao.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Created by xiaoyy on 11/1/17.
 */
@Controller
public class SettingController {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @RequestMapping(path = {"/setting"})
    @ResponseBody
    public String setting() {

        return "setting: OK";
    }
    @RequestMapping(path = {"/header"})
    public String header() {

        return "header";
    }
}
