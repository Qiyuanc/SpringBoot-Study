package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName Hello
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/10 10:44
 * @Version 1.0
 **/
@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/h1")
    @ResponseBody
    public String Test(){
        return "Hello,World!";
    }

    @RequestMapping("/test")
    public String Test1(){
        return "test";
    }
}
