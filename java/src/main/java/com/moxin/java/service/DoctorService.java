package com.moxin.java.service;

import com.moxin.java.pojo.dto.LoginDTO;
import com.moxin.java.pojo.dto.RePasswordDTO;
import com.moxin.java.pojo.dto.RegisterDTO;
import com.moxin.java.pojo.dto.UpdateAvatarDTO;
import com.moxin.java.pojo.entity.Doctor;
import com.moxin.java.pojo.vo.DoctorLoginVO;

public interface DoctorService {
    void register(RegisterDTO registerDTO, String uuid);

    DoctorLoginVO login(LoginDTO loginDTO);

    void updateAvatar(UpdateAvatarDTO updateAvatarDTO);

    void updatePassword(RePasswordDTO rePasswordDTO);

    Doctor getInfo();
}
