package com.moxin.java.service;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> listAll();

    void delete(AppointDeleteDTO appointDeleteDTO);

    void add(Department department);

    void update(Department department);
}
