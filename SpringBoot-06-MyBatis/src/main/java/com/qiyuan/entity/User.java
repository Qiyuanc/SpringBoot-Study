package com.qiyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName User
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/17 17:58
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 属性名与数据库字段名对应
    private int id;
    private String name;
    private String pwd;
}
