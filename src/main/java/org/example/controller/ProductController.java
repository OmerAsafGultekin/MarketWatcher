package org.example.controller;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class ProductController {

    private final ProductRepository repository;

    // Constructor Injection (Best Practice)
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the latest market data for tracked cryptocurrencies.
     * This endpoint returns the most recent price entries from the database.
     *
     * @return A list of the 3 most recently updated Product entities.
     */
    @GetMapping
    public List<Product> getAllPrices() {
        return repository.findTop3ByOrderByCreatedAtDesc();
    }
}