package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.AppointmentMapper;
import com.moxin.java.mapper.DoctorMapper;
import com.moxin.java.mapper.PatientMapper;
import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointDocGetDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.dto.DoctorUpdateAppointmentDTO;
import com.moxin.java.pojo.entity.Appointment;
import com.moxin.java.pojo.entity.Patient;
import com.moxin.java.pojo.vo.AppointmentLoadVO;
import com.moxin.java.pojo.vo.DocGetAppointPatientVO;
import com.moxin.java.service.AppointmentService;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PatientMapper patientMapper;


    @Override
    public void submit(AppointmentSubmissionDTO appointmentSubmissionDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        //时间应该在当前时间之后
        if (appointmentSubmissionDTO.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ResultCode.FAIL, "预约时间不合法");
        }
        Long id = ((Integer) map.get("id")).longValue();
        Appointment appointment = new Appointment();
        appointment.setPatientId(id);
        appointment.setDepartmentId(appointmentSubmissionDTO.getDepartmentId());
        appointment.setDoctorId(appointmentSubmissionDTO.getDoctorId());
        appointment.setReason(appointmentSubmissionDTO.getReason());
        appointment.setAppointmentDate(appointmentSubmissionDTO.getAppointmentDate());
        appointment.setStatus("PENDING");
        appointment.setIsRegistered(false);

        try {
            appointmentMapper.insert(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "提交预约失败");
        }
    }

    @Override
    public List<AppointmentLoadVO> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }

        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", id);
        List<Appointment> appointments = appointmentMapper.selectList(queryWrapper);

        List<AppointmentLoadVO> appointmentLoadVOS = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentLoadVO appointmentLoadVO = new AppointmentLoadVO();
            appointmentLoadVO.setId(appointment.getId());
            appointmentLoadVO.setDoctorName(doctorMapper.selectById(appointment.getDoctorId()).getName());
            appointmentLoadVO.setAppointmentDate(appointment.getAppointmentDate());
            appointmentLoadVO.setStatus(appointment.getStatus());
            appointmentLoadVOS.add(appointmentLoadVO);
        }

        return appointmentLoadVOS;
    }

    @Override
    public void delete(AppointDeleteDTO appointDeleteDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }

        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", appointDeleteDTO.getId());
        queryWrapper.eq("patient_id", id);
        Appointment appointment = appointmentMapper.selectOne(queryWrapper);
        if (appointment == null) {
            throw new AppException(ResultCode.FAIL, "预约不存在");
        }
        try {
            appointmentMapper.deleteById(appointment.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "已经挂号，无法删除");
        }

    }

    @Override
    public List<Appointment> doctorList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }


        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", id);
        queryWrapper.ne("status", "FAIL");
        return appointmentMapper.selectList(queryWrapper);
    }

    @Override
    public DocGetAppointPatientVO getPatient(AppointDocGetDTO appointDocGetDTO) {

        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }

        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", appointDocGetDTO.getId());
        queryWrapper.eq("doctor_id", id);
        Appointment appointment = appointmentMapper.selectOne(queryWrapper);
        if (appointment == null) {
            throw new AppException(ResultCode.FAIL, "预约不存在");
        }

        Patient patient = patientMapper.selectById(appointment.getPatientId());

        DocGetAppointPatientVO docGetAppointPatientVO = new DocGetAppointPatientVO();
        docGetAppointPatientVO.setName(patient.getName());
        docGetAppointPatientVO.setGender(patient.getGender());
        docGetAppointPatientVO.setAge(patient.getAge());
        docGetAppointPatientVO.setContactInfo(patient.getContactInfo());
        docGetAppointPatientVO.setReason(appointment.getReason());

        return docGetAppointPatientVO;

    }

    @Override
    public void updateStatus(DoctorUpdateAppointmentDTO doctorUpdateAppointmentDTO) {

        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }

        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", doctorUpdateAppointmentDTO.getId());
        queryWrapper.eq("doctor_id", id);
        Appointment appointment = appointmentMapper.selectOne(queryWrapper);
        if (appointment == null) {
            throw new AppException(ResultCode.FAIL, "预约不存在");
        }

        if (doctorUpdateAppointmentDTO.getStatus().equals("PENDING") || doctorUpdateAppointmentDTO.getStatus().equals("FAIL") || doctorUpdateAppointmentDTO.getStatus().equals("SUCCESS")) {
            appointment.setStatus(doctorUpdateAppointmentDTO.getStatus());
            appointmentMapper.updateById(appointment);
        } else {
            throw new AppException(ResultCode.FAIL, "状态不合法");
        }

    }
}
