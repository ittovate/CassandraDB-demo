package com.example.cassandradbdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CassandraDbDemoApplication {
    private final static Logger log = LoggerFactory.getLogger(CassandraDbDemoApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CassandraDbDemoApplication.class, args);
        Dotenv dotenv = (Dotenv) context.getBean("dotenv");
    }


}
