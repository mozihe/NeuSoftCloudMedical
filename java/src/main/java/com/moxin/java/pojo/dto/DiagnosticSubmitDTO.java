package com.moxin.java.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticSubmitDTO {
    private String medicalRecordNumber;
    private String diagnosis;
    private String reason;
    private List<DiagnosticSubPreDTO> medicineList;
}
