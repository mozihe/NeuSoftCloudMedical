package com.moxin.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxin.java.exception.AppException;
import com.moxin.java.mapper.PaymentMapper;
import com.moxin.java.pojo.entity.Payment;
import com.moxin.java.service.PaymentService;
import com.moxin.java.utils.ResultCode;
import com.moxin.java.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

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
}
