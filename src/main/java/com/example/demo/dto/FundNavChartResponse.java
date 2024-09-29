package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FundNavChartResponse {

    private int statusCode;
    @JsonProperty("Message") 
    private String message;
    @JsonProperty("Data")
    private List<FundData> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FundData> getData() {
        return data;
    }

    public void setData(List<FundData> data) {
        this.data = data;
    }

    public static class FundData {
        private String name;
        private String shortName;
        private String id;  // Product ID
        private boolean dataGrouping;
        private List<List<Double>> data;  // date and price

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isDataGrouping() {
            return dataGrouping;
        }

        public void setDataGrouping(boolean dataGrouping) {
            this.dataGrouping = dataGrouping;
        }

        public List<List<Double>> getData() {
            return data;
        }

        public void setData(List<List<Double>> data) {
            this.data = data;
        }
    }
}
