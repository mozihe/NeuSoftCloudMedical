package com.moxin.java.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("prescriptions")
public class Prescription {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long diagnosticReportId;
    private Long medicationId;
    private String medicationName;
    private String dosage;
    private String createdAt;
    private String updatedAt;
}
