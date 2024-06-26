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
@TableName("payments")
public class Payment {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Long patientId;
    String medicalRecordNumber;
    float registrationFee;
    float totalAmountDue;
    Boolean isPaymentComplete;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
