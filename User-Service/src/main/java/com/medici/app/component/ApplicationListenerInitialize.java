package com.medici.app.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.medici.app.constants.ApplicationConstants.*;

@Slf4j
@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent> {

    private final Environment environment;

    public ApplicationListenerInitialize(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(ACTIVE_SPRING_PROFILES, Arrays.toString(environment.getActiveProfiles()));
        log.info(SPRING_PROFILES_ACTIVE, environment.getProperty("spring.profiles.active"));
        log.info(APPLICATION_HAS_STARTED);
    }
}
