package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/21 23:44
 * @Version 1.0
 **/
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String Hello(Model model){
        model.addAttribute("msg","<h1>Hello,SpringBoot and Thymeleaf!</h1>");
        model.addAttribute("users", Arrays.asList("Qiyuan","Qiyuanc"));
        return "hello";
    }

}
