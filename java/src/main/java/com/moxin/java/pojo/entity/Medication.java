package com.moxin.java.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("medications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private float price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
