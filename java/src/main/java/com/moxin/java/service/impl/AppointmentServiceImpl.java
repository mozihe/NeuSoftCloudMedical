package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.AppointmentMapper;
import com.moxin.java.mapper.DoctorMapper;
import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.AppointmentSubmissionDTO;
import com.moxin.java.pojo.entity.Appointment;
import com.moxin.java.pojo.vo.AppointmentLoadVO;
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
        appointment.setRegistered(false);

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
}
