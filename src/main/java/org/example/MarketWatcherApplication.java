package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables Spring's scheduled task execution
public class MarketWatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketWatcherApplication.class, args);
    }
}
