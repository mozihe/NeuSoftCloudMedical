package com.moxin.java.service;

import com.moxin.java.pojo.dto.*;
import com.moxin.java.pojo.entity.Doctor;
import com.moxin.java.pojo.vo.DoctorLoginVO;

import java.util.List;

public interface DoctorService {
    void register(RegisterDTO registerDTO, String uuid);

    DoctorLoginVO login(LoginDTO loginDTO);

    void updateAvatar(UpdateAvatarDTO updateAvatarDTO);

    void updatePassword(RePasswordDTO rePasswordDTO);

    Doctor getInfo();

    List<Doctor> getDepartmentDoctorList(DepartmentDoctorDTO departmentDoctorDTO);

    List<Doctor> getAllVerifiedDoctor();
}
