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
@TableName("applications")
public class Application {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long doctorId;
    private String name;
    private String appliedRole;
    private String status;
    private LocalDateTime applicationDate;
}
