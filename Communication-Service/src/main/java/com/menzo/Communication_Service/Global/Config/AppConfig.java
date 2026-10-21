package com.menzo.Communication_Service.Global.Config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(OtpProperties.class)
public class AppConfig {

    //  UTC everywhere so expiry maths doesn't depend on the host's time zone; also trivial to fake in tests.
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
