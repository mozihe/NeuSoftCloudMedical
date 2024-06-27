package com.moxin.java.pojo.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWithNameVO {

        Long id;
        Long patientId;
        String medicalRecordNumber;
        String patientName;
        float registrationFee;
        float totalAmountDue;
        Boolean isPaymentComplete;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;

}
