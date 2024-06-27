package com.moxin.java.controller;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.entity.Medication;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/delete")
    public Result delete(@RequestBody AppointDeleteDTO appointDeleteDTO) {
        medicationService.delete(appointDeleteDTO);
        return Result.builder().code(200).message("success").build();
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Medication medication) {
        medicationService.add(medication);
        return Result.builder().code(200).message("success").build();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Medication medication) {
        medicationService.update(medication);
        return Result.builder().code(200).message("success").build();
    }
}