package com.moxin.java.controller;

import com.moxin.java.pojo.dto.SubmissionDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody SubmissionDTO submissionDTO) {
        applicationService.submit(submissionDTO);
        return Result.builder().code(200).message("提交成功").build();
    }

    @RequestMapping("/list")
    public Result list() {
        return Result.builder().code(200).message("查询成功").data(applicationService.list()).build();
    }
}
