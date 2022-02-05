package com.qiyuan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName JDBCController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/17 10:45
 * @Version 1.0
 **/
// 复习：返回 JSON 字符串，不经过视图解析器
@RestController
public class JDBCController {

    // 已经配置好了，直接拿过来用
    @Autowired
    JdbcTemplate jdbcTemplate;

    // 查询数据库的所有信息
    // 还没写实体类，怎么获取数据库中的对象？使用 Map！
    @GetMapping("/userList")
    public List<Map<String,Object>> userList(){
        // 查询 user 表的所有信息
        String sql = "select * from user";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    @GetMapping("/addUser")
    public String addUser(){
        String sql = "insert into mybatis.user(id,name,pwd) values (4,'qiyuanb','123456')";
        jdbcTemplate.update(sql);
        return "add-ok";
    }

    // 用一下 RestFul 风格
    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id")int id){
        // 预编译，需要传参数
        String sql = "update mybatis.user set name=?,pwd=? where id="+id;
        Object object[] = new Object[2];
        object[0]="qiyuanc";
        object[1]="0823";
        jdbcTemplate.update(sql,object);
        return "update-ok";
    }
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id")int id){
        String sql = "delete from mybatis.user where id=?";
        jdbcTemplate.update(sql,id);
        return "delete-ok";
    }
}
