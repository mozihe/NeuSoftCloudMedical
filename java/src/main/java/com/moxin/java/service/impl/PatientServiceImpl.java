package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.PatientMapper;
import com.moxin.java.pojo.dto.LoginDTO;
import com.moxin.java.pojo.dto.RePasswordDTO;
import com.moxin.java.pojo.dto.RegisterDTO;
import com.moxin.java.pojo.dto.UpdateAvatarDTO;
import com.moxin.java.pojo.entity.Patient;
import com.moxin.java.service.PatientService;
import com.moxin.java.utils.BCryptUtil;
import com.moxin.java.utils.JwtUtil;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PatientServiceImpl implements PatientService {

    @Value("${default.avatar.url}")
    private String defaultAvatarUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public void register(RegisterDTO registerDTO, String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码已过期，请重新获取");
        }

        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", registerDTO.getEmail());
        Patient existingPatient = patientMapper.selectOne(queryWrapper);
        if (existingPatient != null) {
            throw new AppException(ResultCode.EMAIL_ERROR, "该邮箱已被注册");
        }

        QueryWrapper<Patient> usernameQueryWrapper = new QueryWrapper<>();
        usernameQueryWrapper.eq("username", registerDTO.getUsername());
        Patient existingPatientByUsername = patientMapper.selectOne(usernameQueryWrapper);
        if (existingPatientByUsername != null) {
            throw new AppException(ResultCode.USERNAME_ERROR, "该用户名已被注册");
        }

        String code = stringRedisTemplate.opsForValue().get(uuid);
        if (code == null) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码已过期，请重新获取");
        }

        if (!code.equals(registerDTO.getCode())) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码错误");
        }

        Patient patient = new Patient();
        patient.setUsername(registerDTO.getUsername());
        patient.setPassword(BCryptUtil.encryptPassword(registerDTO.getPassword()));
        patient.setEmail(registerDTO.getEmail());
        patient.setName("未实名");
        patient.setContactInfo("未填写");
        patient.setGender("未填写");
        patient.setAge("未填写");
        patient.setIdNumber("未填写");
        patient.setIsProfileComplete(false);

        patient.setAvatarUrl(defaultAvatarUrl);

        try {
            patientMapper.insert(patient);
        } catch (Exception e) {
            throw new AppException(ResultCode.FAIL, "注册失败");
        }
    }

    @Override
    public String login(LoginDTO loginDTO) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        Patient patient = patientMapper.selectOne(queryWrapper);
        if (patient == null) {
            throw new AppException(ResultCode.USERNAME_ERROR, "用户名不存在");
        }

        if (!BCryptUtil.matchPassword(loginDTO.getPassword(), patient.getPassword())) {
            throw new AppException(ResultCode.PASSWORD_ERROR, "密码错误");
        }

        Map<String, Object> claims = Map.of("id", patient.getId(), "username", patient.getUsername(), "role", "patient");
        String token = JwtUtil.genToken(claims);

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token, token, 24, TimeUnit.HOURS);

        return token;
    }

    @Override
    public void updateAvatar(UpdateAvatarDTO updateAvatarDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();

        Patient patient = patientMapper.selectById(id);
        patient.setAvatarUrl(updateAvatarDTO.getAvatar());

        patientMapper.updateById(patient);

    }

    @Override
    public void updatePassword(RePasswordDTO rePasswordDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();

        Patient patient = patientMapper.selectById(id);
        if (!BCryptUtil.matchPassword(rePasswordDTO.getOldPwd(), patient.getPassword())) {
            throw new AppException(ResultCode.PASSWORD_ERROR, "原密码错误");
        }

        patient.setPassword(BCryptUtil.encryptPassword(rePasswordDTO.getNewPwd()));
        patientMapper.updateById(patient);

    }

    @Override
    public Patient getInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (!role.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();

        System.out.println(id);

        return patientMapper.selectById(id);
    }
}
