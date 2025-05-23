package com.moxin.java.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("diagnostic_reports")
public class DiagnosticReport {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String medicalRecordNumber;
    private String diagnosis;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
