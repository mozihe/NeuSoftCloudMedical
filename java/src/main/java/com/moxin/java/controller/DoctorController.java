package com.moxin.java.controller;

import com.moxin.java.pojo.dto.*;
import com.moxin.java.pojo.vo.DoctorLoginVO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.DoctorService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @RequestMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO, @RequestHeader("MailCode") String uuid) {
        doctorService.register(registerDTO, uuid);
        return Result.builder().code(ResultCode.SUCCESS).message("注册成功").build();
    }

    @RequestMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        DoctorLoginVO doctorLoginVO = doctorService.login(loginDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("登录成功").data(doctorLoginVO).build();
    }

    @RequestMapping("/update/avatar")
    public Result updateAvatar(@RequestBody UpdateAvatarDTO updateAvatarDTO) {
        doctorService.updateAvatar(updateAvatarDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("更新头像成功").build();
    }

    @RequestMapping("/update/password")
    public Result updatePassword(@RequestBody RePasswordDTO rePasswordDTO) {
        doctorService.updatePassword(rePasswordDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("更新密码成功").build();
    }

    @RequestMapping("/info")
    public Result getInfo() {
        return Result.builder().code(ResultCode.SUCCESS).message("获取信息成功").data(doctorService.getInfo()).build();
    }

    @RequestMapping("/department/list")
    public Result getDepartmentList(@RequestBody DepartmentDoctorDTO departmentDoctorDTO) {
        return Result.builder().code(ResultCode.SUCCESS).message("获取对应科室医生列表成功").data(doctorService.getDepartmentDoctorList(departmentDoctorDTO)).build();
    }

    @RequestMapping("/look/list")
    public Result getAllVerifiedDoctor() {
        return Result.builder().code(ResultCode.SUCCESS).message("获取所有已认证医生列表成功").data(doctorService.getAllVerifiedDoctor()).build();
    }

}
