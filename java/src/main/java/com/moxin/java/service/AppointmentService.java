package com.moxin.java.service;

import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointDocGetDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.dto.DoctorUpdateAppointmentDTO;
import com.moxin.java.pojo.entity.Appointment;
import com.moxin.java.pojo.vo.AppointmentLoadVO;
import com.moxin.java.pojo.vo.DocGetAppointPatientVO;

import java.util.List;

public interface AppointmentService {
    void submit(AppointmentSubmissionDTO appointmentSubmissionDTO);

    List<AppointmentLoadVO> list();

    void delete(AppointDeleteDTO appointDeleteDTO);

    List<Appointment> doctorList();

    DocGetAppointPatientVO getPatient(AppointDocGetDTO appointDocGetDTO);

    void updateStatus(DoctorUpdateAppointmentDTO doctorUpdateAppointmentDTO);
}
