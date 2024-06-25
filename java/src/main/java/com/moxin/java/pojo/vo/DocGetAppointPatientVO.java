package com.moxin.java.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocGetAppointPatientVO {
    private String name;
    private String gender;
    private String age;
    private String reason;
    private String contactInfo;
}
