package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Price;
import com.example.demo.repository.PriceRepository;
import com.example.demo.service.PriceChangeCalculator;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceChangeCalculator priceChangeCalculator;

    // Query the price for a specific product on a specific date
    @GetMapping("/{date}")
    public ResponseEntity<Price> getPriceByDate(@PathVariable String date) {
        LocalDate queryDate = LocalDate.parse(date);
        Price price = priceRepository.findByProductIdAndDate(10480016L, queryDate)
                .stream().findFirst().orElse(null);

        if (price != null) {
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{date}")
    public ResponseEntity<Price> updatePrice(@PathVariable String date, @RequestBody BigDecimal newPrice) {
        LocalDate queryDate = LocalDate.parse(date);
        Price price = priceRepository.findByProductIdAndDate(10480016L, queryDate)
                .stream().findFirst().orElse(null);

        if (price != null) {
            price.setPrice(newPrice);
            priceRepository.save(price);
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{date}")
    public ResponseEntity<Price> addPrice(@PathVariable String date, @RequestBody BigDecimal priceValue) {
        LocalDate queryDate = LocalDate.parse(date);
        
        // Check if price already exists for this date
        List<Price> existingPrices = priceRepository.findByProductIdAndDate(10480016L, queryDate);
        if (!existingPrices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);  // Conflict if price already exists
        }

        Price newPrice = new Price();
        newPrice.setProductId(10480016L);
        newPrice.setDate(queryDate);
        newPrice.setPrice(priceValue);

        Price savedPrice = priceRepository.save(newPrice);
        return ResponseEntity.ok(savedPrice);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> deletePrice(@PathVariable String date) {
        LocalDate queryDate = LocalDate.parse(date);
        List<Price> prices = priceRepository.findByProductIdAndDate(10480016L, queryDate);

        if (!prices.isEmpty()) {
            priceRepository.deleteAll(prices);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     // API to calculate price change and percentage change between two dates
    @GetMapping("/change")
    public ResponseEntity<Map<String, BigDecimal>> getPriceChange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Price startPrice = priceRepository.findByProductIdAndDate(10480016L, start)
                .stream().findFirst().orElse(null);
        Price endPrice = priceRepository.findByProductIdAndDate(10480016L, end)
                .stream().findFirst().orElse(null);
                
        if (startPrice == null || endPrice == null) {
            return ResponseEntity.notFound().build();
        }

        // Calculate price change and percentage change
        BigDecimal priceChange = priceChangeCalculator.calculatePriceChange(startPrice.getPrice(), endPrice.getPrice());
        BigDecimal percentageChange = priceChangeCalculator.calculatePriceChangePercentage(startPrice.getPrice(), endPrice.getPrice());

        // Return the results
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("priceChange", priceChange);
        response.put("percentageChange", percentageChange);

        return ResponseEntity.ok(response);
    }
}

