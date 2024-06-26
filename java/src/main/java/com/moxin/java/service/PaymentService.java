package com.moxin.java.service;

import com.moxin.java.pojo.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> getPatientPayment();
}
