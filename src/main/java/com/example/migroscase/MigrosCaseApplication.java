package com.example.migroscase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MigrosCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrosCaseApplication.class, args);
    }

}
