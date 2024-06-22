package com.moxin.java;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.moxin.java.mapper")
class JavaApplicationTests {

    @Test
    void contextLoads() {
    }

}
