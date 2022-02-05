package com.qiyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Person
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/18 11:53
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component //注册为 bean
@ConfigurationProperties(prefix = "person")
//@PropertySource(value = "classpath:person.properties")
@Validated
public class Person {
    @Email()
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
