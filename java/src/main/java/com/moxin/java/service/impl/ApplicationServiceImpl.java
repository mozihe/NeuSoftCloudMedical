package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.ApplicationMapper;
import com.moxin.java.mapper.DepartmentMapper;
import com.moxin.java.mapper.DoctorMapper;
import com.moxin.java.pojo.dto.AppointDeleteDTO;
import com.moxin.java.pojo.dto.DoctorUpdateAppointmentDTO;
import com.moxin.java.pojo.dto.SubmissionDTO;
import com.moxin.java.pojo.entity.Application;
import com.moxin.java.pojo.entity.Doctor;
import com.moxin.java.pojo.vo.ApplicationListVO;
import com.moxin.java.pojo.vo.DoctorWithDepartmentNameVO;
import com.moxin.java.service.ApplicationService;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public void submit(SubmissionDTO submissionDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();

        try {
            Doctor doctor = doctorMapper.selectById(id);
            doctor.setName(submissionDTO.getName());
            doctor.setIdNumber(submissionDTO.getIdNumber());
            doctor.setContactInfo(submissionDTO.getContactInfo());
            doctor.setDepartmentId(submissionDTO.getDepartmentId());
            doctor.setIntroduction(submissionDTO.getIntroduction());
            doctorMapper.updateById(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "更新用户信息失败");
        }

        try {
            Application application = new Application();

            application.setDoctorId(id);
            application.setName(submissionDTO.getName());
            application.setAppliedRole(submissionDTO.getTargetRole());
            application.setStatus("ING");
            applicationMapper.insert(application);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResultCode.FAIL, "提交申请失败");
        }

    }

    @Override
    public List<ApplicationListVO> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("admin") && !role.equals("drug") && !role.equals("expert") && !role.equals("unverified"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("doctor_id", id);
        List<Application> applications = applicationMapper.selectList(wrapper);

        List<ApplicationListVO> applicationListVOS = new ArrayList<>();

        for (Application application : applications) {
            ApplicationListVO applicationListVO = new ApplicationListVO();
            applicationListVO.setApplicationTime(application.getApplicationDate());
            applicationListVO.setStatus(application.getStatus());
            applicationListVOS.add(applicationListVO);
        }

        return applicationListVOS;
    }

    @Override
    public List<Application> allList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("admin"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        //这里排除已经拒绝的申请
        wrapper.ne("status", "FAL");

        return applicationMapper.selectList(wrapper);
    }

    @Override
    public DoctorWithDepartmentNameVO getDoctorInfo(AppointDeleteDTO appointDeleteDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("admin"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        //这是application表的id
        Long id = appointDeleteDTO.getId();
        Application application = applicationMapper.selectById(id);
        Doctor doctor = doctorMapper.selectById(application.getDoctorId());
        //太史了，下次一定想好在写
        DoctorWithDepartmentNameVO doctorWithDepartmentNameVO = new DoctorWithDepartmentNameVO();
        doctorWithDepartmentNameVO.setId(doctor.getId());
        doctorWithDepartmentNameVO.setName(doctor.getName());
        doctorWithDepartmentNameVO.setDepartmentId(doctor.getDepartmentId());
        doctorWithDepartmentNameVO.setUsername(doctor.getUsername());
        doctorWithDepartmentNameVO.setContactInfo(doctor.getContactInfo());
        doctorWithDepartmentNameVO.setIntroduction(doctor.getIntroduction());
        doctorWithDepartmentNameVO.setDepartmentName(departmentMapper.selectById(doctor.getDepartmentId()).getName());
        doctorWithDepartmentNameVO.setAvatarUrl(doctor.getAvatarUrl());
        doctorWithDepartmentNameVO.setCreatedAt(doctor.getCreatedAt());
        doctorWithDepartmentNameVO.setEmail(doctor.getEmail());
        doctorWithDepartmentNameVO.setIdNumber(doctor.getIdNumber());
        doctorWithDepartmentNameVO.setPassword(doctor.getPassword());
        doctorWithDepartmentNameVO.setRole(doctor.getRole());
        doctorWithDepartmentNameVO.setUpdatedAt(doctor.getUpdatedAt());
        doctorWithDepartmentNameVO.setVerified(doctor.getVerified());

        return doctorWithDepartmentNameVO;
    }

    @Override
    public void updateStatus(DoctorUpdateAppointmentDTO doctorUpdateAppointmentDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("admin"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = doctorUpdateAppointmentDTO.getId();
        Application application = applicationMapper.selectById(id);
        String status = application.getStatus();
        if (!(status.equals("ING") || status.equals("FAL") || status.equals("SUC"))) {
            throw new AppException(ResultCode.FAIL, "状态不合法");
        }
        application.setStatus(doctorUpdateAppointmentDTO.getStatus());
        applicationMapper.updateById(application);

        if (doctorUpdateAppointmentDTO.getStatus().equals("SUC")) {
            Doctor doctor = doctorMapper.selectById(application.getDoctorId());
            doctor.setRole(application.getAppliedRole());
            doctor.setVerified(true);
            doctorMapper.updateById(doctor);
        }

    }
}
