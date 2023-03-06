package com.xmap.ar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xmap.ar.mapper")
@SpringBootApplication
public class ArApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArApplication.class, args);
    }

}
