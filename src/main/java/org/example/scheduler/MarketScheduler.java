package org.example.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.example.repository.ProductRepository;
import org.example.service.MarketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class MarketScheduler {

    private final MarketService marketService;
    private final ProductRepository repository;

    // List of crypto symbols to track
    private final List<String> symbols = List.of("BTCUSDT", "ETHUSDT", "SOLUSDT");

    // Constructor Injection
    public MarketScheduler(MarketService marketService, ProductRepository repository) {
        this.marketService = marketService;
        this.repository = repository;
    }

    // Fetches prices every 1 minute (60000 ms)
    @Scheduled(fixedRate = 60000)
    public void updatePricesJob() {
        log.info("--- Starting Price Update Cycle ---");

        symbols.stream()
                .map(marketService::fetchPrice)       // Fetch data for each symbol
                .filter(Objects::nonNull)             // Filter out failed requests
                .forEach(repository::save);           // Save successful data to DB
    }

    // Database Cleanup Job
    // Runs every hour. Initial delay ensures DB connection is stable before first run.
    @Scheduled(fixedRate = 3600000, initialDelayString = "10000")
    public void cleanupJob() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        log.warn("[CLEANUP] Removing data older than: {}", threshold);

        repository.deleteByCreatedAtBefore(threshold);
    }
}