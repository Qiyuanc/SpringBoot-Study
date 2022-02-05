package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/4 17:03
 * @Version 1.0
 **/
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    // 添加 Seesion 参数
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        HttpSession session, Model model){
        // 简单的登录判断
        if(!StringUtils.isEmpty(username) && "0723".equals(password)){
            // return "dashboard";
            // 向 Session 中写入信息（该方法要添加 HttpSession 参数才行）
            session.setAttribute("loginUser",username);
            return "redirect:/dashboard";
        }
        else{
            model.addAttribute("msg","用户名或密码错误");
            // 首页用 post 方式发起请求即可
            return "index";
        }
    }
}
