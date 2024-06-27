package com.moxin.java.controller;

import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.PaymentService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/get")
    public Result getPatient() {
        return Result.builder().code(ResultCode.SUCCESS).message("success").data(paymentService.getPatientPayment()).build();
    }

    @RequestMapping("/getall")
    public Result getAllPatient() {
        return Result.builder().code(ResultCode.SUCCESS).message("success").data(paymentService.getAllPatientPayment()).build();
    }

    @RequestMapping("/finish")
    public Result finishPayment(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        paymentService.finishPayment(medicalRecordDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("success").build();
    }

    @RequestMapping("/getadmin")
    public Result getAdminPayment() {
        return Result.builder().code(ResultCode.SUCCESS).message("success").data(paymentService.getAdminPayment()).build();
    }

}

