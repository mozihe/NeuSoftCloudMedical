package com.moxin.java.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocGetDiaVO {
    private String targetName;
    private String medicalRecordNumber;
    private String diagnosis;
}
