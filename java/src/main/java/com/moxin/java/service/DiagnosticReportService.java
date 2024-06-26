package com.moxin.java.service;

import com.moxin.java.pojo.dto.DiagnosticSubmitDTO;
import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.entity.DiagnosticReport;
import com.moxin.java.pojo.entity.Prescription;
import com.moxin.java.pojo.vo.DocGetAppointPatientVO;
import com.moxin.java.pojo.vo.DocGetDiaVO;
import com.moxin.java.pojo.vo.PatGetDiaVO;
import com.moxin.java.pojo.vo.PrescriptionWithPayVO;

import java.util.List;

public interface DiagnosticReportService {
    void submit(DiagnosticSubmitDTO diagnosticSubmitDTO);

    List<DocGetDiaVO> docGetDiagnosticList();

    DocGetAppointPatientVO getDiaPatientInfo(MedicalRecordDTO data);

    List<PrescriptionWithPayVO> getDiaMedicineList(MedicalRecordDTO data);

    List<PatGetDiaVO> getPatientDiagnosticList();
}
