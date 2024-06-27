package com.moxin.java.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorWithDepartmentNameVO {

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
        private String idNumber;
        private Boolean verified;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String departmentName;
}
