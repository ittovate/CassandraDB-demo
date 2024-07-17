package com.example.cassandradbdemo;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CassandraDbDemoApplication {
//    private final static Logger log = LoggerFactory.getLogger(CassandraDbDemoApplication.class);

    public static void main(String[] args) {
            ApplicationContext context = SpringApplication.run(CassandraDbDemoApplication.class, args);
            Dotenv dotenv = (Dotenv) context.getBean("dotenv");

//        try (CqlSession session = CqlSession.builder()
//                .withCloudSecureConnectBundle(Paths.get(System.getProperty("user.dir")+"/src/main/resources/secure-connect-inboxapp.zip"))
//                .withAuthCredentials("ZxitGXlgyjJRikxEBkWmIjdC","c2lv9C8ht59aB.k6XemjrxT36tBfv1rGU,b8CO2hUbwg7ZquDUN.10QI._QpEsY6d8GmWJxoDiqHAhc.aP+xs8Njm7H6h-ni.KQZ-tBbHzHtD_5bA.LJUPhjovJT-RnN")
//                .build()) {
//            // Select the release_version from the system.local table:
//            ResultSet rs = session.execute("select release_version from system.local");
//            Row row = rs.one();
//            //Print the results of the CQL query to the console:
//            if (row != null) {
//                System.out.println(row.getString("release_version"));
//            } else {
//                System.out.println("An error occurred.");
//            }
//        }
//        System.exit(0);



    }
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }


}
