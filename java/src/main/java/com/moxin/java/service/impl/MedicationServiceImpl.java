package com.moxin.java.service.impl;

import com.moxin.java.mapper.MedicationMapper;
import com.moxin.java.pojo.entity.Medication;
import com.moxin.java.service.MedicationService;
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
}
