package com.example.demo.controller;

import com.example.demo.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FundController {

    @Autowired
    private FundService fundService;

    @GetMapping("/api/fetch-fund-data")
    public String fetchFundData() {
        fundService.fetchAndStoreFundData();
        return "Fund data fetched and stored successfully!";
    }
}
