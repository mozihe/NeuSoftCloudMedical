package com.moxin.java.controller;

import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.PaymentService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
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

}
