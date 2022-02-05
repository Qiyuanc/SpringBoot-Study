package com.qiyuan.controller;

import com.qiyuan.dao.DepartmentDao;
import com.qiyuan.dao.EmployeeDao;
import com.qiyuan.entity.Department;
import com.qiyuan.entity.Employee;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * @ClassName EmployeeController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/5 13:52
 * @Version 1.0
 **/
@Controller
public class EmployeeController {

    // 没有 Service 层，先用 Dao 层凑个数
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    // 查询所有员工
    @RequestMapping("/list")
    public String getAllEmployeesList(Model model){
        Collection<Employee> employees = employeeDao.getEmployees();
        model.addAttribute("emps",employees);
        return "emp/list";
    }

    // 跳转到增加员工页面
    @RequestMapping("/toAdd")
    public String toAddPage(Model model){
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/add.html";
    }

    // 增加员工操作
    @RequestMapping("/addEmployee")
    public String addEmployee(Employee employee){
        // 调用业务层（没有！）
        employeeDao.addEmployee(employee);
        return "redirect:/list";
    }

    // 跳转到修改员工页面，路径为 REST 风格
    @RequestMapping("/toUpdate/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model){
        // 查询要修改的员工的信息
        Employee employee = employeeDao.getEmployeeById(id);
        // 放到模型中，给前端页面显示
        model.addAttribute("emp",employee);
        // 部门信息也要带上
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/update";
    }

    @RequestMapping("/updateEmployee")
    public String updateEmployee(Employee employee){
        // 直接使用 add 的原因是数据库其实是个 Map
        // add 时若 Key 即 ID 已存在，则会对应到这个新的上面来
        employeeDao.addEmployee(employee);
        return "redirect:/list";
    }

    @RequestMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id){
        employeeDao.deleteEmployee(id);
        return "redirect:/list";
    }
}
