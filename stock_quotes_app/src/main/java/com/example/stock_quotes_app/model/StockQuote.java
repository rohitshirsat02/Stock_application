package com.example.stock_quotes_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuote {
    private String symbol;               // The stock symbol (e.g., AAPL)
    private double price;                // The latest price
    private double change;               // The price change from the previous close
    private double percentageChange;     // The percentage change from the previous close
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;     // The time of the quote
    private double open;                 // The opening price
    private double high;                 // The highest price in the interval
    private double low;                  // The lowest price in the interval
    private double close;                // The closing price in the interval
    private long volume;                 // The trading volume
}
