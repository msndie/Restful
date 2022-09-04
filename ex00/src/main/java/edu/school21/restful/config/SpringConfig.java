package edu.school21.restful.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("edu.school21.restful.repositories")
//@ComponentScan("edu.school21.restful")
public class SpringConfig {
}
