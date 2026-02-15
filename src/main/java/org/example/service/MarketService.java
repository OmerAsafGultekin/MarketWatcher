package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MarketService {

    private final WebClient webClient;
    private final ObjectMapper mapper;

    // Spring's @Scheduled runs on a single thread by default.
    // Using standard HashMap and ArrayDeque is both safe and more performant here.
    private final Map<String, Deque<BigDecimal>> priceHistoryCache = new HashMap<>();
    private static final int HISTORY_SIZE = 5;

    // Constructor Injection (Modern Spring Best Practice)
    public MarketService(WebClient.Builder webClientBuilder, ObjectMapper mapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.binance.com").build();
        this.mapper = mapper;
    }

    /**
     * Fetches real-time price from Binance API and analyzes the market trend.
     *
     * @param symbol The trading pair symbol (e.g., BTCUSDT)
     * @return Product entity containing price and trend analysis, or null if fetch fails.
     */
    public Product fetchPrice(String symbol) {
        try {
            String jsonResponse = webClient.get()
                    .uri("/api/v3/ticker/price?symbol=" + symbol)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonNode = mapper.readTree(jsonResponse);
            String retrievedSymbol = jsonNode.get("symbol").asText();
            BigDecimal currentPrice = new BigDecimal(jsonNode.get("price").asText());

            // Analyze trend based on Simple Moving Average (SMA)
            String trendStatus = analyzeTrend(retrievedSymbol, currentPrice);

            return new Product(retrievedSymbol, currentPrice, trendStatus);

        } catch (Exception e) {
            log.error("[ERROR] Failed to fetch data for {}: {}", symbol, e.getMessage());
            return null;
        }
    }

    /**
     * Calculates the Simple Moving Average (SMA) and determines the trend.
     */
    private String analyzeTrend(String symbol, BigDecimal currentPrice) {
        priceHistoryCache.putIfAbsent(symbol, new ArrayDeque<>());
        Deque<BigDecimal> history = priceHistoryCache.get(symbol);

        // Add new price to the head of the deque
        history.offerFirst(currentPrice);

        // Keep only the latest records defined by HISTORY_SIZE
        if (history.size() > HISTORY_SIZE) {
            history.pollLast(); // Remove oldest
        }

        // Insufficient data for analysis
        if (history.size() < HISTORY_SIZE) {
            log.info("{} gathering data ({}/{})", symbol, history.size(), HISTORY_SIZE);
            return "GATHERING DATA";
        }

        // Calculate Average
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal price : history) {
            sum = sum.add(price);
        }

        BigDecimal average = sum.divide(BigDecimal.valueOf(HISTORY_SIZE), 2, RoundingMode.HALF_UP);

        // Determine Trend
        if (currentPrice.compareTo(average) > 0) {
            log.info("{} UPTREND ",symbol);
            return "UPTREND";
        } else if (currentPrice.compareTo(average) < 0) {
            log.info("{} DOWNTREND ",symbol);
            return "DOWNTREND";
        } else {
            log.info("{} STABLE ",symbol);
            return "STABLE";
        }
    }
}