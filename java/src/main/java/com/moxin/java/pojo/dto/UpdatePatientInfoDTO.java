package com.moxin.java.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePatientInfoDTO {
    private String name;
    private String contactInfo;
    private String gender;
    private String age;
    private String idNumber;
}
