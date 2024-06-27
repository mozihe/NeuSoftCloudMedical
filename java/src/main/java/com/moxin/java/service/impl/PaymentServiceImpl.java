package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.PatientMapper;
import com.moxin.java.mapper.PaymentMapper;
import com.moxin.java.pojo.dto.MedicalRecordDTO;
import com.moxin.java.pojo.entity.Payment;
import com.moxin.java.pojo.vo.PaymentWithNameVO;
import com.moxin.java.service.PaymentService;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public List<Payment> getPatientPayment() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("patient")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        Long id = ((Integer) map.get("id")).longValue();
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", id);
        return paymentMapper.selectList(queryWrapper);
    }

    @Override
    public List<PaymentWithNameVO> getAllPatientPayment() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("drug")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }

        //借助mybatis-plus官方实现，for循环增加字段patientName,除此之外，不限制用户id，但是只返回未完成的订单
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_payment_complete", 0);
        List<Payment> payments = paymentMapper.selectList(queryWrapper);

        List<PaymentWithNameVO> paymentWithNameVOS = new ArrayList<>();

        for (Payment payment : payments) {
            PaymentWithNameVO paymentWithNameVO = new PaymentWithNameVO();
            paymentWithNameVO.setIsPaymentComplete(false);
            paymentWithNameVO.setPatientId(payment.getPatientId());
            paymentWithNameVO.setPatientName(patientMapper.selectById(payment.getPatientId()).getName());
            paymentWithNameVO.setCreatedAt(payment.getCreatedAt());
            paymentWithNameVO.setUpdatedAt(payment.getUpdatedAt());
            paymentWithNameVO.setId(payment.getId());
            paymentWithNameVO.setMedicalRecordNumber(payment.getMedicalRecordNumber());
            paymentWithNameVO.setRegistrationFee(payment.getRegistrationFee());
            paymentWithNameVO.setTotalAmountDue(payment.getTotalAmountDue());
            paymentWithNameVOS.add(paymentWithNameVO);
        }

        return paymentWithNameVOS;


    }

    @Override
    public void finishPayment(MedicalRecordDTO medicalRecordDTO) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("drug")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }

        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_number", medicalRecordDTO.getMedicalRecordNumber());
        Payment payment = paymentMapper.selectOne(queryWrapper);
        if (payment == null) {
            throw new AppException(ResultCode.NOT_FOUND, "未找到该订单");
        }
        payment.setIsPaymentComplete(true);
        paymentMapper.updateById(payment);

    }

    @Override
    public List<Payment> getAdminPayment() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("role");
        if (!username.equals("admin")) {
            throw new AppException(ResultCode.UNAUTHORIZED, "权限不足");
        }
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        return paymentMapper.selectList(queryWrapper);
    }
}
