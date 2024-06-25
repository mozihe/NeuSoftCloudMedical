package com.moxin.java.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDoctorInfoDTO {
    private String name;
    private String contactInfo;
    private String introduction;
}
