package com.CptFranck.SportsPeak;

import com.CptFranck.SportsPeak.config.graphql.DgsProperties;
import com.CptFranck.SportsPeak.config.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        StorageProperties.class,
        DgsProperties.class
})
public class SportsPeakApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsPeakApplication.class, args);
    }

}
