package com.moxin.java.service;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.vo.AppointmentLoadVO;

import java.util.List;

public interface AppointmentService {
    void submit(AppointmentSubmissionDTO appointmentSubmissionDTO);

    List<AppointmentLoadVO> list();

    void delete(AppointDeleteDTO appointDeleteDTO);
}
