package com.example.withoutdesignpatterns.performance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SortByTitlePerformanceTest {

    private static final String URL = "http://localhost:8082/api/books/sort/title";
    private static final int NUMBER_OF_REQUESTS = 500; // Number of GET requests

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        long totalStartTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        long totalTime = 0;
        int successfulRequests = 0;

        for (int i = 1; i <= NUMBER_OF_REQUESTS; i++) {
            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = sendGetRequest(restTemplate);
            long endTime = System.currentTimeMillis();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                successfulRequests++;
                totalTime += (endTime - startTime);
            }
        }

        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        long totalEndTime = System.currentTimeMillis();

        System.out.println("Performance Test Results:");
        System.out.println("Total Time: " + (totalEndTime - totalStartTime) + " ms");
        System.out.println("Average Time Per Request: " + (totalTime / (double) successfulRequests) + " ms");
        System.out.println("Memory Used: " + ((endMemory - startMemory) / 1024 / 1024) + " MB");
        System.out.println("Successful Requests: " + successfulRequests);
    }

    private static ResponseEntity<String> sendGetRequest(RestTemplate restTemplate) {
        try {
            return restTemplate.getForEntity(URL, String.class);
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}