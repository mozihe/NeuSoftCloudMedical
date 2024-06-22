package com.moxin.java.controller;

import com.moxin.java.pojo.dto.LoginDTO;
import com.moxin.java.pojo.dto.RePasswordDTO;
import com.moxin.java.pojo.dto.RegisterDTO;
import com.moxin.java.pojo.dto.UpdateAvatarDTO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.PatientService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO, @RequestHeader("MailCode") String uuid) {
        patientService.register(registerDTO, uuid);
        return Result.builder().code(ResultCode.SUCCESS).message("注册成功").build();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        String token = patientService.login(loginDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("登录成功").data(token).build();
    }

    @RequestMapping("/update/avatar")
    public Result updateAvatar(@RequestBody UpdateAvatarDTO updateAvatarDTO) {
        patientService.updateAvatar(updateAvatarDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("更新头像成功").build();
    }

    @RequestMapping("/update/password")
    public Result updatePassword(@RequestBody RePasswordDTO rePasswordDTO) {
        patientService.updatePassword(rePasswordDTO);
        return Result.builder().code(ResultCode.SUCCESS).message("更新密码成功").build();
    }

    @RequestMapping("/info")
    public Result getInfo() {
        return Result.builder().code(ResultCode.SUCCESS).message("获取信息成功").data(patientService.getInfo()).build();
    }
}
