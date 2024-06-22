package com.moxin.java.controller;


import com.moxin.java.pojo.vo.EmailVO;
import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.EmailService;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public Result sendEmail(@RequestBody @Validated EmailVO email) {
        String uuid = emailService.sendEmail(email.getEmail());
        return Result.builder().code(ResultCode.SUCCESS).message("邮件发送成功").data(uuid).build();
    }
}
