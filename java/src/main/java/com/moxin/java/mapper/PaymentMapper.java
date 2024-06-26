package com.moxin.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxin.java.pojo.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
