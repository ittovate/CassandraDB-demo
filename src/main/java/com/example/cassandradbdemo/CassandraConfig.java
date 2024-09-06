//package com.example.cassandradbdemo;
//import com.datastax.oss.driver.api.core.CqlSessionBuilder;
//import com.example.cassandradbdemo.repos.EmailListItemRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
//import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
//import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
//
//import java.nio.file.Path;
//
//@Configuration
////@EnableCassandraRepositories(basePackages = "com.example.cassandradbdemo.repos")
//public class CassandraConfig extends AbstractCassandraConfiguration {
//
//    private final DataStaxAstraProperties astraProperties;
//
////    @Autowired
////    private EmailListItemRepository emailListItemRepository;
//    public CassandraConfig(DataStaxAstraProperties astraProperties) {
//        this.astraProperties = astraProperties;
//    }
//
//    @Override
//    protected String getKeyspaceName() {
//        System.out.println("ASTRA_KEYSPACE:::   " + System.getProperty("ASTRA_KEYSPACE"));
//
//        return System.getProperty("ASTRA_KEYSPACE");
//    }
//
//    @Bean
//    public CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
//        Path bundle = astraProperties.getSecureConnectBundle().toPath();
//        return builder -> builder.withCloudSecureConnectBundle(bundle);
//    }
//
//
////    @Bean
////    public CqlSessionFactoryBean session() {
////        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
//////        session.setKeyspaceName(getKeyspaceName());
////        return session;
////    }
//
//
//
//}
