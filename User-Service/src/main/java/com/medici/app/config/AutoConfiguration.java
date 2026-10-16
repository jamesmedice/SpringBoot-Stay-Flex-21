package com.medici.app.config;

import com.medici.app.entity.User;
import com.medici.app.repository.UserRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.medici.app.entity"}, basePackageClasses = {User.class})
@EnableJpaRepositories(basePackages = {"com.medici.app.repository"}, basePackageClasses = {UserRepository.class})
@ComponentScan(basePackages = {"com.medici.app"})
public class AutoConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
