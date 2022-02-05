package com.qiyuan.dao;

import com.qiyuan.entity.Department;
import com.qiyuan.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName EmployeeDao
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/3 23:13
 * @Version 1.0
 **/
@Repository
public class EmployeeDao {
    // 用 Map 模拟数据库中的数据，key 就相当于主键了
    private static Map<Integer, Employee> employeeMap = null;

    // 自动装配
    @Autowired
    private DepartmentDao departmentDao;

    // 静态代码段，类初始化时就加载
    static {
        employeeMap = new HashMap<>();
        employeeMap.put(1001,new Employee(1001,"A","A@email",0,new Department(101,"教学部门")));
        employeeMap.put(1002,new Employee(1002,"B","B@email",1,new Department(102,"市场部门")));
        employeeMap.put(1003,new Employee(1003,"C","C@email",0,new Department(103,"研发部门")));
        employeeMap.put(1004,new Employee(1004,"D","D@email",1,new Department(104,"运营部门")));
        employeeMap.put(1005,new Employee(1005,"E","E@email",0,new Department(105,"后勤部门")));
    }

    // 主键自增
    private static Integer initId = 1006;

    // 增加员工
    public void addEmployee(Employee employee){
        if(employee.getId()==null)
            employee.setId(initId++);
        if(employee == null)
            System.out.println("员工为空！！！");
        else
            System.out.println(employee);
        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));

        employeeMap.put(employee.getId(),employee);
    }

    // 查询所有员工
    public Collection<Employee> getEmployees(){
        return employeeMap.values();
    }

    // 根据 id 查询员工
    public Employee getEmployeeById(Integer id){
        return employeeMap.get(id);
    }

    // 删除员工
    public void deleteEmployee(Integer id){
        employeeMap.remove(id);
    }
}
