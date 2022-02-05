package com.qiyuan;

import com.qiyuan.service.UserService;
import com.qiyuan.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot08ShiroApplicationTests {

    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() {
        System.out.println(getDigitalSum(25));
    }

    public int getDigitalSum(int num){
        int sum = 0;
        while(num!=0){
            sum += num % 10;
            num = num / 10;
        }
        return sum;
    }

}
