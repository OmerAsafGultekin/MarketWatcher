package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Retrieves the latest 3 records from the database.
     * Since we track 3 coins, this effectively gets the latest status for all of them.
     */
    List<Product> findTop3ByOrderByCreatedAtDesc();

    /**
     * Deletes all records older than the specified date.
     * @param expiryDate The threshold date for deletion.
     */
    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime expiryDate);
}