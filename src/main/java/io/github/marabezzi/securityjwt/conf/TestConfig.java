package io.github.marabezzi.securityjwt.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.marabezzi.securityjwt.service.DBService;
import lombok.AllArgsConstructor;

@Configuration
@Profile("test")
@AllArgsConstructor
public class TestConfig {
 final private DBService dbService;
 
 @Bean
 public void instaciaDB() {
	 this.dbService.instanciaDB();
 }
}
