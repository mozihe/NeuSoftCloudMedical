package com.moxin.java.controller;

import com.moxin.java.pojo.dto.RegistrationSubmissionDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.RegistrationService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody RegistrationSubmissionDTO registrationSubmissionDTO) {
        registrationService.submit(registrationSubmissionDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("提交成功").build();
    }

    @RequestMapping("/pre")
    public Result getPreInfo() {
        return Result.builder().code(ResultCode.SUCCESS).message("获取成功").data(registrationService.getPreInfo()).build();
    }

}
