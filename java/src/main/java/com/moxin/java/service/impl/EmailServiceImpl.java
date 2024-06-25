package com.moxin.java.service.impl;

import com.moxin.java.exception.AppException;
import com.moxin.java.service.EmailService;
import com.moxin.java.utils.EmailUtils;
import com.moxin.java.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String sendEmail(String email) {
        Integer code = EmailUtils.genEmailCode();
        String message = EmailUtils.genEmailMessage(code);
        String subject = EmailUtils.genEmailSubject();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("zhujunheng2005@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new AppException(ResultCode.EMAIL_ERROR, "邮件发送失败");
        }
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(uuid, code.toString(), 60 * 5, TimeUnit.SECONDS);
        return uuid;
    }
}
