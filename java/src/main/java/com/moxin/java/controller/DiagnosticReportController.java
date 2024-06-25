package com.moxin.java.controller;

import com.moxin.java.pojo.dto.DiagnosticSubmitDTO;
import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.DiagnosticReportService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diagnosticreport")
public class DiagnosticReportController {

    @Autowired
    DiagnosticReportService diagnosticReportService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody DiagnosticSubmitDTO diagnosticSubmitDTO) {
        diagnosticReportService.submit(diagnosticSubmitDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("提交成功").build();
    }

    @RequestMapping("/doctor/list")
    public Result docGetDiagnosticList() {
        return Result.builder().code(ResultCode.SUCCESS).message("获取成功").data(diagnosticReportService.docGetDiagnosticList()).build();
    }

    @RequestMapping("/doctor/getpatient")
    public Result getDiaPatientInfo(@RequestBody MedicalRecordDTO data) {
        return Result.builder().code(ResultCode.SUCCESS).message("获取成功").data(diagnosticReportService.getDiaPatientInfo(data)).build();
    }

    @RequestMapping("/medicine/list")
    public Result getDiaMedicineList(@RequestBody MedicalRecordDTO data) {
        return Result.builder().code(ResultCode.SUCCESS).message("获取成功").data(diagnosticReportService.getDiaMedicineList(data)).build();
    }


}


