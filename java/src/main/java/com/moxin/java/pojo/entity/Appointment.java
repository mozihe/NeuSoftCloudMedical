package com.moxin.java.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("appointments")
public class Appointment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long departmentId;
    private LocalDateTime appointmentDate;
    private String status;
    private String reason;
    private boolean isRegistered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String medicalRecordNumber;
}
