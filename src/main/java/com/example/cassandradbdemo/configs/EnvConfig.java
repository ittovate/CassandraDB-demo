package com.example.cassandradbdemo.configs;

import io.github.cdimascio.dotenv.DotenvEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvConfig {

    private Dotenv dotEnv;

    EnvConfig() {
        dotEnv = Dotenv.load();
        for (DotenvEntry entry : dotEnv.entries()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    @Bean
    public Dotenv dotenv() {
        return dotEnv;
    }

}

