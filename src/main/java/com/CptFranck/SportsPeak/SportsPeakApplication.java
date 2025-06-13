package com.CptFranck.SportsPeak;

import com.CptFranck.SportsPeak.config.storage.StorageProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
public class SportsPeakApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsPeakApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
