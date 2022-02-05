package com.qiyuan.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;


/**
 * @ClassName BookServiceImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/31 22:39
 * @Version 1.0
 **/
// 作为 Dubbo 的服务发布到注册中心
@Service
// 将本类放到 Spring 容器中
@Component
public class BookServiceImpl implements BookService{
    @Override
    public String getBook() {
        return "Qiyuanc的笔记";
    }
}
