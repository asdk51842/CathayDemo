package com.example.demo.repository;

import com.example.demo.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByProductIdAndDate(Long productId, LocalDate date);
}
