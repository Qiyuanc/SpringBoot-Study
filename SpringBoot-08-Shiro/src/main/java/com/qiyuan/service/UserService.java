package com.qiyuan.service;

import com.qiyuan.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User queryUserByName(String name);
}
