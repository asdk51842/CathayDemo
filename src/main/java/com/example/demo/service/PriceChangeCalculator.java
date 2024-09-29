package com.example.demo.service;

import java.math.BigDecimal;

public interface PriceChangeCalculator {

    BigDecimal calculatePriceChange(BigDecimal previousPrice, BigDecimal currentPrice);
    BigDecimal calculatePriceChangePercentage(BigDecimal previousPrice, BigDecimal currentPrice);
}