package com.github.edgger.alfabattle.task4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "com.github.edgger.alfabattle.task4.repository")
@Configuration
public class Config {

}
