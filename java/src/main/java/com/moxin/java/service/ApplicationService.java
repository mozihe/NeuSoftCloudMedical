package com.moxin.java.service;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.DoctorUpdateAppointmentDTO;
import com.moxin.java.pojo.dto.SubmissionDTO;
import com.moxin.java.pojo.entity.Application;
import com.moxin.java.pojo.vo.ApplicationListVO;
import com.moxin.java.pojo.vo.DoctorWithDepartmentNameVO;

import java.util.List;

public interface ApplicationService {
    void submit(SubmissionDTO submissionDTO);

    List<ApplicationListVO> list();

    List<Application> allList();

    DoctorWithDepartmentNameVO getDoctorInfo(AppointDeleteDTO appointDeleteDTO);

    void updateStatus(DoctorUpdateAppointmentDTO doctorUpdateAppointmentDTO);
}
