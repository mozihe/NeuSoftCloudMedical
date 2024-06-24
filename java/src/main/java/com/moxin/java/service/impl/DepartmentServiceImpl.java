package com.moxin.java.service.impl;

import com.moxin.java.mapper.DepartmentMapper;
import com.moxin.java.pojo.entity.Department;
import com.moxin.java.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;
    @Override
    public List<Department> listAll() {
        return departmentMapper.selectList(null).stream().filter(department -> department.getId() != 1).collect(Collectors.toList());
    }
}
