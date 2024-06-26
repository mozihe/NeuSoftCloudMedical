package com.moxin.java.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionWithPayVO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long diagnosticReportId;
    private Long medicationId;
    private float price;
    private String medicationName;
    private String dosage;
    private String createdAt;
    private String updatedAt;
}
