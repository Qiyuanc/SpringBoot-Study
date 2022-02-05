package com.qiyuan.mapper;

import com.qiyuan.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
// 表示本接口是 MyBatis 的接口，必须添加才能被 Spring 放到容器中！
@Mapper
public interface UserMapper {
    public User queryUserByName(String name);
}
