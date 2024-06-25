package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.*;
import com.moxin.java.pojo.dto.RegistrationSubmissionDTO;
import com.moxin.java.pojo.entity.Appointment;
import com.moxin.java.pojo.entity.Patient;
import com.moxin.java.pojo.entity.Registration;
import com.moxin.java.pojo.vo.NextRegistrationVO;
import com.moxin.java.pojo.vo.RegistrationPreVO;
import com.moxin.java.service.RegistrationService;
import com.moxin.java.utils.GeneralRecordNumberUtil;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    RegistrationMapper registrationMapper;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    AppointmentMapper appointmentMapper;


    @Override
    public void submit(RegistrationSubmissionDTO registrationSubmissionDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();

        Patient patient = patientMapper.selectById(id);
        if (!patient.getIsProfileComplete()) {
            throw new AppException(ResultCode.FAIL, "请先完善个人信息");
        }

        Registration registration = new Registration();
        registration.setPatientId(id);
        registration.setDepartmentId(registrationSubmissionDTO.getDepartmentId());
        registration.setDoctorId(registrationSubmissionDTO.getDoctorId());
        registration.setReason(registrationSubmissionDTO.getReason());
        registration.setAppointment(registrationSubmissionDTO.getIsAppointment());
        registration.setMedicalRecordNumber(GeneralRecordNumberUtil.generate());
        //这里如果有预约还要判断预约码是否正确，之后处理预约状态
        if (registrationSubmissionDTO.getIsAppointment()) {
            QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_id", id);
            queryWrapper.eq("department_id", registrationSubmissionDTO.getDepartmentId());
            queryWrapper.eq("doctor_id", registrationSubmissionDTO.getDoctorId());
            queryWrapper.eq("is_registered", false);
            queryWrapper.eq("id", registrationSubmissionDTO.getAppointId());
            //预约状态应该是SUCCESS
            queryWrapper.eq("status", "SUCCESS");
            Appointment appointment = appointmentMapper.selectOne(queryWrapper);
            if (appointment == null) {
                throw new AppException(ResultCode.FAIL, "预约码错误");
            }
            appointment.setIsRegistered(true);
            appointment.setMedicalRecordNumber(registration.getMedicalRecordNumber());
            appointmentMapper.updateById(appointment);
        }
        try {
            registrationMapper.insert(registration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "提交失败");
        }
    }

    @Override
    public RegistrationPreVO getPreInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", id);

        Registration registration = registrationMapper.selectOne(queryWrapper);
        RegistrationPreVO registrationPreVO = new RegistrationPreVO();
        if (registration == null) {
            registrationPreVO.setIfAppointment(false);
            return registrationPreVO;
        } else {
            registrationPreVO.setIfAppointment(true);

            registrationPreVO.setDepartmentName(departmentMapper.selectById(registration.getDepartmentId()).getName());
            registrationPreVO.setDoctorName(doctorMapper.selectById(registration.getDoctorId()).getName());
            registrationPreVO.setMedicalRecord(registration.getMedicalRecordNumber());

            QueryWrapper<Registration> queryWrapperGetPeopleNum = new QueryWrapper<>();
            //挂号时间在该挂号时间之前的
            queryWrapperGetPeopleNum.le("created_at", registration.getCreatedAt());
            //同一个医生
            queryWrapperGetPeopleNum.eq("doctor_id", registration.getDoctorId());
            //同一个科室
            queryWrapperGetPeopleNum.eq("department_id", registration.getDepartmentId());

            registrationPreVO.setWaitPeople(registrationMapper.selectCount(queryWrapperGetPeopleNum).intValue() - 1);
            return registrationPreVO;

        }
    }

    @Override
    public NextRegistrationVO getNextDoctor() {
        //找到对应医生的下一个挂号，这是挂号通道，应该是没有预约的人，并且事created_at最早的
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();
        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", id);
        queryWrapper.eq("is_appointment", false);
        queryWrapper.orderByAsc("created_at");
        Registration registration = registrationMapper.selectOne(queryWrapper);
        NextRegistrationVO nextRegistrationVO = new NextRegistrationVO();
        try {
            if (registration == null) {
                nextRegistrationVO.setIsEmpty(true);
                return nextRegistrationVO;
            } else {
                nextRegistrationVO.setIsEmpty(false);
                nextRegistrationVO.setMedicalRecordNumber(registration.getMedicalRecordNumber());
                nextRegistrationVO.setName(patientMapper.selectById(registration.getPatientId()).getName());
                nextRegistrationVO.setRegistrationReason(registration.getReason());
                nextRegistrationVO.setAge(patientMapper.selectById(registration.getPatientId()).getAge());
                nextRegistrationVO.setGender(patientMapper.selectById(registration.getPatientId()).getGender());
                nextRegistrationVO.setContactInfo(patientMapper.selectById(registration.getPatientId()).getContactInfo());
                return nextRegistrationVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "获取失败");
        }
    }
}
