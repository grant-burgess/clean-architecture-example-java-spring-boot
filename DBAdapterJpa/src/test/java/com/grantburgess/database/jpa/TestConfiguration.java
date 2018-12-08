package com.grantburgess.database.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.grantburgess.database.jpa.data")
@EnableJpaRepositories("com.grantburgess.database.jpa.repositories")
public class TestConfiguration { }
