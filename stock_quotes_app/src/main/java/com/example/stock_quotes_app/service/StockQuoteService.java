package com.example.stock_quotes_app.service;

import com.example.stock_quotes_app.model.StockQuote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.time.LocalDateTime;

@Service
public class StockQuoteService {

    private final WebClient webClient;

    @Value("${stock.api.base-url}")
    private String apiUrl;

    @Value("${stock.api.key}")
    private String apiKey;

    //constructor intialize  webclient with the baseurl and builder for URL
    public StockQuoteService(WebClient.Builder webClientBuilder, @Value("${stock.api.base-url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }
    
    public Mono<StockQuote> getStockQuote(String symbol) {      //FETCHING the stock forgiven smbl
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "TIME_SERIES_INTRADAY")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", "5min")
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    System.out.println("Raw API Response: " + response);
                    
                    ObjectMapper mapper = new ObjectMapper();
                    StockQuote stockQuote = new StockQuote();
                    try {
                        JsonNode root = mapper.readTree(response);
    
                        // Check for API errors
                        if (root.has("Note")) {
                            throw new RuntimeException("API rate limit exceeded: " + root.path("Note").asText());
                        } else if (root.has("Error Message")) {
                            throw new RuntimeException("Error fetching stock quote: " + root.path("Error Message").asText());
                        }
    
                        JsonNode metaData = root.path("Meta Data");
                        String extractedSymbol = metaData.path("2. Symbol").asText();
                        JsonNode timeSeries = root.path("Time Series (5min)");
    
                        if (timeSeries.isEmpty()) {
                            throw new RuntimeException("No time series data found for symbol: " + symbol);
                        }
    
                        // Get the two most recent time points stocks for show on the screen
                        Iterator<String> timePoints = timeSeries.fieldNames();
                        String latestTime = timePoints.next();  // Get the latest timestamp or time
                        JsonNode latestData = timeSeries.path(latestTime);
                        double latestClose = latestData.path("4. close").asDouble();
                        double open = latestData.path("1. open").asDouble(); // Opening price of the stcok
                        double high = latestData.path("2. high").asDouble(); // Highest price of the stock
                        double low = latestData.path("3. low").asDouble();   // Lowest price of the stock
                        long volume = latestData.path("5. volume").asLong();  // Volume of the given stock

                        String previousTime = timePoints.hasNext() ? timePoints.next() : latestTime;  // Get the previous timestamp
                        JsonNode previousData = timeSeries.path(previousTime);
                        double previousClose = previousData.path("4. close").asDouble(); // Previous close price
    
                        // Calculate price change and percentage change
                        double change = latestClose - previousClose;
                        double percentageChange = (change / previousClose) * 100;
    
                        // Set values of the stock to StockQuote object or in the model file store the values
                        stockQuote.setSymbol(extractedSymbol);
                        stockQuote.setPrice(latestClose);
                        stockQuote.setChange(change);
                        stockQuote.setPercentageChange(percentageChange);
                        stockQuote.setTimestamp(LocalDateTime.now());
                        stockQuote.setOpen(open);
                        stockQuote.setHigh(high);
                        stockQuote.setLow(low);
                        stockQuote.setClose(latestClose); // Set latest close as current close
                        stockQuote.setVolume(volume);
                    } catch (Exception e) {
                        System.err.println("Error parsing stock quote response: " + e.getMessage());
                        throw new RuntimeException("Error parsing stock quote response", e);
                    }
                    return stockQuote;
                })
                .onErrorResume(e -> {
                    System.err.println("Error fetching stock quote: " + e.getMessage());
                    return Mono.error(new RuntimeException("Error fetching stock quote"));
                });
    }
}
