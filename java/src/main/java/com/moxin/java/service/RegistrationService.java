package com.moxin.java.service;

import com.moxin.java.pojo.dto.RegistrationSubmissionDTO;
import com.moxin.java.pojo.vo.RegistrationPreVO;

public interface RegistrationService {
    void submit(RegistrationSubmissionDTO registrationSubmissionDTO);

    RegistrationPreVO getPreInfo();
}
