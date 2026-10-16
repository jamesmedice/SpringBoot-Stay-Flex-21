package com.medici.app.config;

import com.medici.app.entity.BankAccount;
import com.medici.app.repository.BankAccountRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.gft.elw.model", "com.gft.habacus.app.model"}, basePackageClasses = {BankAccount.class})
@EnableJpaRepositories(basePackages = {"com.gft.elw.repository"}, basePackageClasses = {BankAccountRepository.class})
@ComponentScan(basePackages = {"com.gft.elw"})
public class AutoConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
