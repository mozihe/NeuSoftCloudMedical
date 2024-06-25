package com.moxin.java.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextRegistrationVO {
    Boolean isEmpty;
    String medicalRecordNumber;
    String name;
    String age;
    String gender;
    String registrationReason;
    String contactInfo;
}
