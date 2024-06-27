package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.*;
import com.moxin.java.pojo.dto.DiagnosticSubPreDTO;
import com.moxin.java.pojo.dto.DiagnosticSubmitDTO;
import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.entity.*;
import com.moxin.java.pojo.vo.DocGetAppointPatientVO;
import com.moxin.java.pojo.vo.DocGetDiaVO;
import com.moxin.java.pojo.vo.PatGetDiaVO;
import com.moxin.java.pojo.vo.PrescriptionWithPayVO;
import com.moxin.java.service.DiagnosticReportService;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DiagnosticReportServiceImpl implements DiagnosticReportService {

    @Autowired
    private DiagnosticReportMapper diagnosticReportMapper;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private MedicationMapper medicationMapper;


    @Override
    public void submit(DiagnosticSubmitDTO diagnosticSubmitDTO) {
        //先判断医生身份，之后把诊断相关信息插入数据库，最后把处方信息插入数据库，之后删去挂号表对应的记录，如果这个挂号有预约，还要把预约表对应的记录删去
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();

        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setDoctorId(id);

        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_number", diagnosticSubmitDTO.getMedicalRecordNumber());
        Registration registration = registrationMapper.selectOne(queryWrapper);
        if (registration == null) {
            throw new AppException(ResultCode.FAIL, "未找到对应挂号信息");
        }
        diagnosticReport.setPatientId(registration.getPatientId());
        diagnosticReport.setMedicalRecordNumber(diagnosticSubmitDTO.getMedicalRecordNumber());
        diagnosticReport.setReason(diagnosticSubmitDTO.getReason());
        diagnosticReport.setDiagnosis(diagnosticSubmitDTO.getDiagnosis());
        diagnosticReportMapper.insert(diagnosticReport);

        if (diagnosticSubmitDTO.getMedicineList() != null && !diagnosticSubmitDTO.getMedicineList().isEmpty()) {
            for (DiagnosticSubPreDTO item : diagnosticSubmitDTO.getMedicineList()) {
                Prescription prescription = new Prescription();
                prescription.setDiagnosticReportId(diagnosticReport.getId());
                prescription.setMedicationId(item.getId());
                prescription.setDosage(item.getUsage());
                prescription.setMedicationName(item.getName());
                prescriptionMapper.insert(prescription);
            }
        }

        Payment payment = new Payment();
        payment.setPatientId(registration.getPatientId());
        payment.setIsPaymentComplete(false);

        Doctor doctor = doctorMapper.selectById(id);

        if (doctor.getRole().equals("expert")) {
            payment.setRegistrationFee(5);
        } else {
            payment.setRegistrationFee(1.5F);
        }
        payment.setMedicalRecordNumber(diagnosticSubmitDTO.getMedicalRecordNumber());

        QueryWrapper<Prescription> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("diagnostic_report_id", diagnosticReport.getId());
        List<Prescription> prescriptions = prescriptionMapper.selectList(queryWrapper2);
        float total = 0F;
        for (Prescription prescription : prescriptions) {
            Medication medication = medicationMapper.selectById(prescription.getMedicationId());
            total += medication.getPrice() * Integer.parseInt(prescription.getDosage());
        }

        payment.setTotalAmountDue(total + payment.getRegistrationFee());
        paymentMapper.insert(payment);

        //先查看是否有预约，处理预约表,预约表中的挂号码是唯一的
        QueryWrapper<Appointment> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("medical_record_number", diagnosticSubmitDTO.getMedicalRecordNumber());
        Appointment appointment = appointmentMapper.selectOne(queryWrapper1);
        if (appointment != null) {
            appointmentMapper.deleteById(appointment.getId());
        }

        registrationMapper.delete(queryWrapper);

    }

    @Override
    public List<DocGetDiaVO> docGetDiagnosticList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("doctor") && !role.equals("unverified") && !role.equals("admin") && !role.equals("drug") && !role.equals("expert"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();
        QueryWrapper<DiagnosticReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", id);

        List<DiagnosticReport> diagnosticReports = diagnosticReportMapper.selectList(queryWrapper);

        List<DocGetDiaVO> docGetDiaVOS = new ArrayList<>();

        for (DiagnosticReport diagnosticReport : diagnosticReports) {
            DocGetDiaVO docGetDiaVO = new DocGetDiaVO();
            docGetDiaVO.setDiagnosis(diagnosticReport.getDiagnosis());
            docGetDiaVO.setTargetName(patientMapper.selectById(diagnosticReport.getPatientId()).getName());
            docGetDiaVO.setMedicalRecordNumber(diagnosticReport.getMedicalRecordNumber());
            docGetDiaVOS.add(docGetDiaVO);
        }

        return docGetDiaVOS;
    }

    @Override
    public DocGetAppointPatientVO getDiaPatientInfo(MedicalRecordDTO data) {
        QueryWrapper<DiagnosticReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_number", data.getMedicalRecordNumber());
        DiagnosticReport diagnosticReport = diagnosticReportMapper.selectOne(queryWrapper);
        if (diagnosticReport == null) {
            throw new AppException(ResultCode.FAIL, "未找到对应诊断信息");
        }

        DocGetAppointPatientVO docGetAppointPatientVO = new DocGetAppointPatientVO();
        //这里应该根据病人id查找病人信息
        Patient patient = patientMapper.selectById(diagnosticReport.getPatientId());
        docGetAppointPatientVO.setAge(patient.getAge());
        docGetAppointPatientVO.setGender(patient.getGender());
        docGetAppointPatientVO.setName(patient.getName());
        docGetAppointPatientVO.setReason(diagnosticReport.getReason());
        docGetAppointPatientVO.setContactInfo(patient.getContactInfo());
        return docGetAppointPatientVO;
    }

    @Override
    public List<PrescriptionWithPayVO> getDiaMedicineList(MedicalRecordDTO data) {
        QueryWrapper<DiagnosticReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_number", data.getMedicalRecordNumber());
        DiagnosticReport diagnosticReport = diagnosticReportMapper.selectOne(queryWrapper);
        if (diagnosticReport == null) {
            throw new AppException(ResultCode.FAIL, "未找到对应诊断信息");
        }

        QueryWrapper<Prescription> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("diagnostic_report_id", diagnosticReport.getId());
        List<Prescription> prescriptions = prescriptionMapper.selectList(queryWrapper1);

        List<PrescriptionWithPayVO> prescriptionWithPayVOS = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            PrescriptionWithPayVO prescriptionWithPayVO = new PrescriptionWithPayVO();
            prescriptionWithPayVO.setMedicationName(prescription.getMedicationName());
            prescriptionWithPayVO.setDosage(prescription.getDosage());
            Medication medication = medicationMapper.selectById(prescription.getMedicationId());
            prescriptionWithPayVO.setPrice(medication.getPrice());
            prescriptionWithPayVO.setDiagnosticReportId(prescription.getDiagnosticReportId());
            prescriptionWithPayVO.setMedicationId(prescription.getMedicationId());
            prescriptionWithPayVO.setCreatedAt(prescription.getCreatedAt());
            prescriptionWithPayVO.setUpdatedAt(prescription.getUpdatedAt());
            prescriptionWithPayVO.setId(prescription.getId());
            prescriptionWithPayVOS.add(prescriptionWithPayVO);
        }

        return prescriptionWithPayVOS;
    }

    @Override
    public List<PatGetDiaVO> getPatientDiagnosticList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("patient"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<DiagnosticReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", id);

        List<DiagnosticReport> diagnosticReports = diagnosticReportMapper.selectList(queryWrapper);

        List<PatGetDiaVO> patGetDiaVOS = new ArrayList<>();

        for (DiagnosticReport diagnosticReport : diagnosticReports) {
            PatGetDiaVO patGetDiaVO = new PatGetDiaVO();
            patGetDiaVO.setDiagnosis(diagnosticReport.getDiagnosis());
            patGetDiaVO.setCreatedAt(diagnosticReport.getCreatedAt());
            patGetDiaVO.setMedicalRecordNumber(diagnosticReport.getMedicalRecordNumber());
            patGetDiaVO.setTargetName(doctorMapper.selectById(diagnosticReport.getDoctorId()).getName());
            patGetDiaVOS.add(patGetDiaVO);
        }

        return patGetDiaVOS;
    }

    @Override
    public List<PatGetDiaVO> adminGetDiagnosticList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String role = (String) map.get("role");
        if (role == null || (!role.equals("admin"))) {
            throw new AppException(ResultCode.UNAUTHORIZED, "无权限");
        }
        Long id = ((Integer) map.get("id")).longValue();

        QueryWrapper<DiagnosticReport> queryWrapper = new QueryWrapper<>();

        List<DiagnosticReport> diagnosticReports = diagnosticReportMapper.selectList(queryWrapper);

        List<PatGetDiaVO> patGetDiaVOS = new ArrayList<>();

        for (DiagnosticReport diagnosticReport : diagnosticReports) {
            PatGetDiaVO patGetDiaVO = new PatGetDiaVO();
            patGetDiaVO.setDiagnosis(diagnosticReport.getDiagnosis());
            patGetDiaVO.setCreatedAt(diagnosticReport.getCreatedAt());
            patGetDiaVO.setMedicalRecordNumber(diagnosticReport.getMedicalRecordNumber());
            patGetDiaVO.setTargetName(doctorMapper.selectById(diagnosticReport.getDoctorId()).getName());
            patGetDiaVOS.add(patGetDiaVO);
        }

        return patGetDiaVOS;

    }

}
