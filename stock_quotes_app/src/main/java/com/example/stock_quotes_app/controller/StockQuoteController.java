package com.example.stock_quotes_app.controller;

import com.example.stock_quotes_app.model.*;
import com.example.stock_quotes_app.service.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stock")  // PATH FOR API AFTER A PORT NO. 
public class StockQuoteController {

    //declare the dependancy for fetching the business logic FROM that class
    private final StockQuoteService stockQuoteService;

    //constructor use to intialize the stockservice object
    public StockQuoteController(StockQuoteService stockQuoteService) {
        this.stockQuoteService = stockQuoteService;
    }

    @GetMapping("/quote/{symbol}")
    public Mono<StockQuote> getStockQuote(@PathVariable String symbol) {
        return stockQuoteService.getStockQuote(symbol);
    }
}

