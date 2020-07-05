package com.github.edgger.alfabattle.task3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaRepositories("com.github.edgger.alfabattle.task3.entity")
@Configuration
public class Config {

}
