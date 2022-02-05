package com.qiyuan.service;

import com.qiyuan.entity.User;
import com.qiyuan.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/24 11:51
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService{

    // 注入 DAO 层对象以进行操作
    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }
}
