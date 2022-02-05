package com.qiyuan.mapper;

import com.qiyuan.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//该注解表示本类是 MyBatis 接口类
@Mapper
@Repository
public interface UserMapper {

    // 查询所有用户
    List<User> queryUserList();
    // 以ID查询一个用户
    User queryUserById(int id);
    // 新增用户
    int addUser(User user);
    // 修改用户
    int updateUser(User user);
    // 删除用户
    int deleteUser(int id);
}
