package com.moxin.java.service;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.entity.Medication;

import java.util.List;

public interface MedicationService {
    List<Medication> list();

    void delete(AppointDeleteDTO appointDeleteDTO);

    void add(Medication medication);

    void update(Medication medication);
}
