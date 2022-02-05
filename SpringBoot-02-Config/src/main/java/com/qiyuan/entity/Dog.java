package com.qiyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName Dog
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/18 11:34
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
// 注册为 bean
@Component
public class Dog {
    @Value("小狗")
    private String name;
    @Value("3")
    private Integer age;
}
