package com.mss;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AnkiTestApplication {

    public static void main(String[] args){

        SpringApplication.run(AnkiTestApplication.class, args);

    }

}
