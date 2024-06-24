package com.moxin.java.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private String name;
    private String idNumber;
    private String contactInfo;
    private Long departmentId;
    private String targetRole;
    private String introduction;
}
