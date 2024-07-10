package com.example.cassandradbdemo;

import com.example.cassandradbdemo.models.Vet;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.repos.VetRepository;
import jakarta.annotation.PostConstruct;
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

    @Bean
    public CommandLineRunner clr(VetRepository vetRepository) {
        return args -> {
            vetRepository.deleteAll();

            Vet john = new Vet(UUID.randomUUID(), "John", "Doe", new HashSet<>(Arrays.asList("surgery")));
            Vet jane = new Vet(UUID.randomUUID(), "Jane", "Doe", new HashSet<>(Arrays.asList("radiology, surgery")));

            Vet savedJohn = vetRepository.save(john);
            Vet savedJane = vetRepository.save(jane);

            vetRepository.findAll()
                    .forEach(v -> log.info("Vet: {}", v.getFirstName()));

            vetRepository.findById(savedJohn.getId())
                    .ifPresent(v -> log.info("Vet by id: {}", v.getFirstName()));
        };
    }



}
