package com.example.mymiaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.mymiaosha.dao")
@SpringBootApplication
public class MymiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MymiaoshaApplication.class, args);
    }

}
