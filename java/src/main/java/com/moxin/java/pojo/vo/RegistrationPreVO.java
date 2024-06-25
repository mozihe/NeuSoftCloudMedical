package com.moxin.java.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationPreVO {
    boolean ifAppointment;
    String doctorName;
    String departmentName;
    String medicalRecord;
    int waitPeople;
}
