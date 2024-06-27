package com.moxin.java.controller;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.entity.Department;
import com.moxin.java.pojo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moxin.java.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/list")
    public Result getAll() {
        return Result.builder().code(200).message("获取成功").data(departmentService.listAll()).build();
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody AppointDeleteDTO appointDeleteDTO) {
        departmentService.delete(appointDeleteDTO);
        return Result.builder().code(200).message("删除成功").build();
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.builder().code(200).message("添加成功").build();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.builder().code(200).message("更新成功").build();
    }

}
