package com.moxin.java.controller;

import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medication")
public class MedicationController {
    @Autowired
    private MedicationService medicationService;

    @RequestMapping("/list")
    public Result list() {
        return Result.builder().code(200).message("success").data(medicationService.list()).build();
    }
}
