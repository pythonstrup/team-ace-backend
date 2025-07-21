package com.nexters.teamace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TeamAceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamAceBackendApplication.class, args);
    }
}
