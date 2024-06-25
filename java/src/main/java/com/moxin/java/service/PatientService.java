package com.moxin.java.service;

import com.moxin.java.pojo.dto.*;
import com.moxin.java.pojo.entity.Patient;

public interface PatientService {
    void register(RegisterDTO registerDTO, String uuid);

    String login(LoginDTO loginDTO);

    void updateAvatar(UpdateAvatarDTO updateAvatarDTO);

    void updatePassword(RePasswordDTO rePasswordDTO);

    Patient getInfo();

    void updateInfo(UpdatePatientInfoDTO updatePatientInfoDTO);
}
