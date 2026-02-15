package org.example.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;

    // Precision 19, Scale 4 allows for prices like 123456789.1234
    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    private String trend; // Values: "UPTREND", "DOWNTREND", "STABLE"

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public Product() {}

    public Product(String symbol, BigDecimal price, String trend) {
        this.symbol = symbol;
        this.price = price;
        this.trend = trend;
    }

    // Getters and Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}