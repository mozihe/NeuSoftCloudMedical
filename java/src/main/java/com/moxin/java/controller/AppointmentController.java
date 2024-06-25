package com.moxin.java.controller;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointDocGetDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.dto.DoctorUpdateAppointmentDTO;
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

    @RequestMapping("/doctor/list")
    public Result doctorList() {
        return Result.builder().code(200).message("查询成功").data(appointmentService.doctorList()).build();
    }

    @RequestMapping("/doctor/getpatient")
    public Result getPatient(@RequestBody AppointDocGetDTO appointDocGetDTO) {
        return Result.builder().code(200).message("查询成功").data(appointmentService.getPatient(appointDocGetDTO)).build();
    }

    @RequestMapping("/doctor/update")
    public Result update(@RequestBody DoctorUpdateAppointmentDTO doctorUpdateAppointmentDTO) {
        appointmentService.updateStatus(doctorUpdateAppointmentDTO);
        return Result.builder().code(200).message("更新成功").build();
    }




}
