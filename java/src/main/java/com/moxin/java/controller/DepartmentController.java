package com.moxin.java.controller;

import com.moxin.java.pojo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
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

}
