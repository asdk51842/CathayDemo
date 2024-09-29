package com.example.demo.service;

import com.example.demo.dto.FundNavChartResponse;
import com.example.demo.entity.Product;
import com.example.demo.entity.Price;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
@Service
public class FundService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    private final Logger logger = LoggerFactory.getLogger(FundService.class);
    private final String API_URL = "https://www.cathaybk.com.tw/cathaybk/service/newwealth/fund/chartservice.asmx/GetFundNavChart";

    public void fetchAndStoreFundData() {
        RestTemplate restTemplate = new RestTemplate();
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"req\":{\"Keys\":[\"10480016\"],\"From\":\"2023/03/10\",\"To\":\"2024/03/10\"}}";

        // Combine headers and body into an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        
       try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            
            logger.info("Response from external API: " + response.getBody());

            // Parse the response into Java objects
            ObjectMapper objectMapper = new ObjectMapper();
            FundNavChartResponse fundNavChartResponse = objectMapper.readValue(response.getBody(), FundNavChartResponse.class);

            for (FundNavChartResponse.FundData fundData : fundNavChartResponse.getData()) {
                Product product = new Product();
                product.setId(Long.parseLong(fundData.getId()));  // 商品 ID
                product.setName(fundData.getName());  // 商品名稱
                product.setShortName(fundData.getShortName());  // 商品短名稱 (nullable)
                product.setIsGrouped(fundData.isDataGrouping());  // 資料是否分組
                productRepository.save(product);

                // Save Price entities for the product
                for (List<Double> priceEntry : fundData.getData()) {
                    long timestamp = priceEntry.get(0).longValue();  // Timestamp (milliseconds)
                    double priceValue = priceEntry.get(1);  // Price value

                    LocalDate date = Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                    // Create and save Price entity
                    Price price = new Price();
                    price.setProductId(product.getId());  // 商品 ID
                    price.setDate(date);  // 日期
                    price.setPrice(BigDecimal.valueOf(priceValue));  // 價格
                    priceRepository.save(price);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching or storing fund data: ", e);
            throw new RuntimeException("Failed to fetch and store fund data", e);
        }
    }
}
