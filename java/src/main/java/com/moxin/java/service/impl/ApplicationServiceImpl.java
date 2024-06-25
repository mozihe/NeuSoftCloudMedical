package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.ApplicationMapper;
import com.moxin.java.mapper.DoctorMapper;
import com.moxin.java.pojo.dto.SubmissionDTO;
import com.moxin.java.pojo.entity.Application;
import com.moxin.java.pojo.entity.Doctor;
import com.moxin.java.pojo.vo.ApplicationListVO;
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
}
