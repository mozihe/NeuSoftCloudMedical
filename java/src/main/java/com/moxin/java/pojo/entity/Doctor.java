package com.moxin.java.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("doctors")
public class Doctor {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long departmentId;
    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String email;
    private String contactInfo;
    private String avatarUrl;
    private String role;
    private String introduction;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
