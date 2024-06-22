package com.moxin.java.controller;

import com.moxin.java.pojo.vo.Result;
import com.moxin.java.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String url = uploadService.upload(file);
        return Result.builder().code(200).message("上传成功").data(url).build();

    }
}
