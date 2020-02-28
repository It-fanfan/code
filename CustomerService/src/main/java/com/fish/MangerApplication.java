package com.fish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
public class MangerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MangerApplication.class, args);
    }
}
