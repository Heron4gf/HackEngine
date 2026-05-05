package it.unicam.ids2026;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackEngineApplication.class, args);
    }
}
