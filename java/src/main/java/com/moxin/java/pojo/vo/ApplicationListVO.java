package com.moxin.java.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationListVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime applicationTime;
    String status;
}
