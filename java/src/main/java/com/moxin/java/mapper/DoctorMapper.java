package com.moxin.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxin.java.pojo.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
}
