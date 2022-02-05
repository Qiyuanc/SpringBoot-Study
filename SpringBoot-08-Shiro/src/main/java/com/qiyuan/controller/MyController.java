package com.qiyuan.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName MyController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/22 13:36
 * @Version 1.0
 **/
@Controller
public class MyController {
    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","Hello,Shiro!");
        return "index";
    }

    @RequestMapping("/user/add")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/user/update")
    public String toUpdate(){
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username,String password,Model model){

        // 获取当前登录的用户对象
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据为 token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try{
            // 当前用户 带着登录数据 执行登录操作！
            // 没有异常，说明登录成功，出现什么异常就是什么问题！
            subject.login(token);
            return "index";
        }catch (UnknownAccountException unknownAccountException){
            // 不存在用户名异常
            model.addAttribute("msg","用户名不存在！");
            return "login";
        }catch (IncorrectCredentialsException incorrectCredentialsException){
            // 身份验证错误异常 密码错误！
            model.addAttribute("msg","密码错误！");
            return "login";
        }

    }

    @RequestMapping("/unauth")
    @ResponseBody
    public String unauthorized(){
        return "未授权无法访问";
    }
}
