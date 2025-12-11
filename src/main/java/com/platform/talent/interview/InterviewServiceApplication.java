package com.platform.talent.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class InterviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewServiceApplication.class, args);
    }
}

