package com.qiyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName Department
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/3 22:58
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Department {
    private Integer id;
    private String departmentName;
}
