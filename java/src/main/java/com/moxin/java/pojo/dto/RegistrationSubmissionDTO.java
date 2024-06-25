package com.moxin.java.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationSubmissionDTO {
    Long departmentId;
    Long doctorId;
    String reason;
    Boolean isAppointment;
    Long appointId;
}
