package com.moxin.java.controller;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody AppointmentSubmissionDTO appointmentSubmissionDTO) {
        appointmentService.submit(appointmentSubmissionDTO);
        return Result.builder().code(200).message("预约成功").build();
    }

    @RequestMapping("/list")
    public Result list() {
        return Result.builder().code(200).message("查询成功").data(appointmentService.list()).build();
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody AppointDeleteDTO appointDeleteDTO) {
        appointmentService.delete(appointDeleteDTO);
        return Result.builder().code(200).message("删除成功").build();
    }

}
