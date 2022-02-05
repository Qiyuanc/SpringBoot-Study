package com.qiyuan.dao;

import com.qiyuan.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DepartmentDao
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/3 23:05
 * @Version 1.0
 **/
@Repository
public class DepartmentDao {
    // 用 Map 模拟数据库中的数据，key 就相当于主键了

    private static Map<Integer, Department> departmentMap = null;

    // 静态代码段，类初始化时就加载
    static {
        departmentMap = new HashMap<>();
        departmentMap.put(101,new Department(101,"教学部门"));
        departmentMap.put(102,new Department(102,"市场部门"));
        departmentMap.put(103,new Department(103,"研发部门"));
        departmentMap.put(104,new Department(104,"运营部门"));
        departmentMap.put(105,new Department(105,"后勤部门"));
    }

    // 查询所有部门信息
    public Collection<Department> getDepartments(){
        return departmentMap.values();
    }

    // 通过 id 查询部门
    public Department getDepartmentById(Integer id){
        return departmentMap.get(id);
    }
}
