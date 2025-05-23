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
@TableName("registrations")
public class Registration {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Long patientId;
    Long doctorId;
    Long departmentId;
    Long appointmentId;
    String reason;
    String medicalRecordNumber;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isAppointment;
}
