package com.moxin.java.service.impl;

import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.MedicationMapper;
import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.entity.Medication;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.MedicationService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationMapper medicationMapper;

    @Override
    public List<Medication> list() {
        return medicationMapper.selectList(null);
    }

    @Override
    public void delete(AppointDeleteDTO appointDeleteDTO) {
        try {
            medicationMapper.deleteById(appointDeleteDTO.getId());
        } catch (Exception e) {
            throw new AppException(ResultCode.FAIL, "与其他数据关联，无法删除");
        }

    }

    @Override
    public void add(Medication medication) {
        medicationMapper.insert(medication);
    }

    @Override
    public void update(Medication medication) {
        medicationMapper.updateById(medication);
    }
}
