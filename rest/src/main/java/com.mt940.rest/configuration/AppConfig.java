package com.mt940.rest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mt940.domain.mt940.MT940Transaction;
import com.mt940.domain.mt940.MT940TransactionSearchRequest;
import com.mt940.rest.dto.TransactionView;
import com.mt940.rest.dto.UserDetailsView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;


@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.mt940.domain"})
@EnableJpaRepositories(basePackages = {"com.mt940.dao"})
@ComponentScan(basePackages = "com.mt940.dao")


public class AppConfig {

    @Value("${spring.datasource.driverClassName}")
    String driverClassName;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .build();
    }

    @Bean("transactionMapperFacade")
    public MapperFacade getTransactionMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory
                .Builder()
                .build();
        factory.registerClassMap(factory.classMap(TransactionView.class,MT940Transaction.class).mapNulls(false).byDefault().toClassMap());
        return factory.getMapperFacade();
    }

    @Bean("userDetailsMapperFacade")
    public MapperFacade getUserDetailsMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory
                .Builder()
                .build();
        factory.registerClassMap(factory.classMap(UserDetailsView.class,UserDetails.class).mapNulls(false).exclude("authorities").byDefault().toClassMap());
        return factory.getMapperFacade();
    }

    @Bean
    ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        return objectMapper;
    }

    @Bean
    MT940TransactionSearchRequest getSearchRequest() {
        return new MT940TransactionSearchRequest();
    }

}