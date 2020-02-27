package com.fish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fish.dao.primary.mapper")
public class MangerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MangerApplication.class, args);
    }
}
