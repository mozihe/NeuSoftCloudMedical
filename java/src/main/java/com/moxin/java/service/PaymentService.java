package com.moxin.java.service;

import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.entity.Payment;
import com.moxin.java.pojo.vo.PaymentWithNameVO;

import java.util.List;

public interface PaymentService {
    List<Payment> getPatientPayment();

    List<PaymentWithNameVO> getAllPatientPayment();

    void finishPayment(MedicalRecordDTO medicalRecordDTO);

    List<Payment> getAdminPayment();
}
