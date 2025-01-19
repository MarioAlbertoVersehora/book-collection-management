package com.example.withoutdesignpatterns.performance;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AddBookPerformanceTest {

    private static final String URL = "http://localhost:8082/api/books";
    private static final int NUMBER_OF_REQUESTS = 10000; // Number of POST requests

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        long totalStartTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        long totalTime = 0;
        int successfulRequests = 0;

        for (int i = 1; i <= NUMBER_OF_REQUESTS; i++) {
            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = sendPostRequest(restTemplate, i);
            long endTime = System.currentTimeMillis();

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
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

    private static ResponseEntity<String> sendPostRequest(RestTemplate restTemplate, int id) {
        String title = "Book Title " + id;
        String author = "Author " + id;
        String genre = "Genre " + id;
        int pages = id * 10;

        String requestBody = "{\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"author\": \"" + author + "\",\n" +
                "  \"genre\": \"" + genre + "\",\n" +
                "  \"pages\": " + pages + "\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        try {
            return restTemplate.postForEntity(URL, request, String.class);
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}
