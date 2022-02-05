package com.qiyuan.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/31 22:20
 * @Version 1.0
 **/
// 交给 Spring
@Service
public class UserService {
    // 消费者要去买书！
    // 远程引用指定的服务，会在注册中心寻找该接口
    @Reference
    BookService bookService;

    public void buyBook(){
        String book = bookService.getBook();
        System.out.println("使用注册中心的服务获得了:"+book+"！");
    }
}
