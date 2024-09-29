package com.example.demo.service;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceChangeCalculatorImpl implements PriceChangeCalculator {

    @Override
    public BigDecimal calculatePriceChange(BigDecimal previousPrice, BigDecimal currentPrice) {
        return currentPrice.subtract(previousPrice);
    }

    @Override
    public BigDecimal calculatePriceChangePercentage(BigDecimal previousPrice, BigDecimal currentPrice) {
        return (currentPrice.subtract(previousPrice))
                .divide(previousPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
