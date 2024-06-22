package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.DoctorMapper;
import com.moxin.java.pojo.dto.LoginDTO;
import com.moxin.java.pojo.dto.RePasswordDTO;
import com.moxin.java.pojo.dto.RegisterDTO;
import com.moxin.java.pojo.dto.UpdateAvatarDTO;
import com.moxin.java.pojo.entity.Doctor;
import com.moxin.java.pojo.vo.DoctorLoginVO;
import com.moxin.java.service.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {

    @Value("${default.avatar.url}")
    private String defaultAvatarUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public void register(RegisterDTO registerDTO, String uuid){
        if (uuid == null || uuid.isEmpty()) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码已过期，请重新获取");
        }

        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", registerDTO.getEmail());
        Doctor existingDoctor = doctorMapper.selectOne(queryWrapper);
        if (existingDoctor != null) {
            throw new AppException(ResultCode.EMAIL_ERROR, "该邮箱已被注册");
        }

        QueryWrapper<Doctor> usernameQueryWrapper = new QueryWrapper<>();
        usernameQueryWrapper.eq("username", registerDTO.getUsername());
        Doctor existingDoctorByUsername = doctorMapper.selectOne(usernameQueryWrapper);
        if (existingDoctorByUsername != null) {
            throw new AppException(ResultCode.USERNAME_ERROR, "该用户名已被注册");
        }

        String code = stringRedisTemplate.opsForValue().get(uuid);
        if (code == null) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码已过期，请重新获取");
        }

        if (!code.equals(registerDTO.getCode())) {
            throw new AppException(ResultCode.EMAIL_ERROR, "验证码错误");
        }

        Doctor doctor = new Doctor();
        doctor.setUsername(registerDTO.getUsername());
        doctor.setPassword(BCryptUtil.encryptPassword(registerDTO.getPassword()));
        doctor.setEmail(registerDTO.getEmail());
        doctor.setAvatarUrl(defaultAvatarUrl);
        doctor.setName("未实名");
        doctor.setContactInfo("未填写");
        doctor.setDepartmentId(1L);
        doctor.setIntroduction("未填写");
        doctor.setRole("unverified");
        doctor.setVerified(false);

        try {
            doctorMapper.insert(doctor);
        } catch (Exception e) {
            throw new AppException(ResultCode.FAIL, "注册失败");
        }

    }

    @Override
    public DoctorLoginVO login(LoginDTO loginDTO) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        Doctor doctor = doctorMapper.selectOne(queryWrapper);
        if (doctor == null) {
            throw new AppException(ResultCode.USERNAME_ERROR, "用户名不存在");
        }
        if (!BCryptUtil.matchPassword(loginDTO.getPassword(), doctor.getPassword())) {
            throw new AppException(ResultCode.PASSWORD_ERROR, "密码错误");
        }
        DoctorLoginVO doctorLoginVO = new DoctorLoginVO();
        doctorLoginVO.setRole(doctor.getRole());

        Map<String, Object> claims = Map.of("id", doctor.getId(), "username", doctor.getUsername(), "role", doctor.getRole());
        String token = JwtUtil.genToken(claims);

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token, token, 24, TimeUnit.HOURS);

        doctorLoginVO.setToken(token);

        return doctorLoginVO;
    }

    @Override
    public void updateAvatar(UpdateAvatarDTO updateAvatarDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        System.out.println(role);
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();
        Doctor doctor = doctorMapper.selectById(id);
        doctor.setAvatarUrl(updateAvatarDTO.getAvatar());
        try {
            doctorMapper.updateById(doctor);
        } catch (Exception e) {
            throw new AppException(ResultCode.FAIL, "更新头像失败");
        }
    }

    @Override
    public void updatePassword(RePasswordDTO rePasswordDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();
        Doctor doctor = doctorMapper.selectById(id);
        if (!BCryptUtil.matchPassword(rePasswordDTO.getOldPwd(), doctor.getPassword())){
            throw new AppException(ResultCode.PASSWORD_ERROR, "原密码错误");
        }
        doctor.setPassword(BCryptUtil.encryptPassword(rePasswordDTO.getNewPwd()));
        try {
            doctorMapper.updateById(doctor);
        } catch (Exception e) {
            throw new AppException(ResultCode.FAIL, "更新密码失败");
        }

    }

    @Override
    public Doctor getInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Integer) map.get("id")).longValue();
        return doctorMapper.selectById(id);
    }
}
