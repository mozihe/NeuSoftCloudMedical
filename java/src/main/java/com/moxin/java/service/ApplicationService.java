package com.moxin.java.service;

import com.moxin.java.pojo.dto.SubmissionDTO;
import com.moxin.java.pojo.vo.ApplicationListVO;

import java.util.List;

public interface ApplicationService {
    void submit(SubmissionDTO submissionDTO);

    List<ApplicationListVO> list();
}
