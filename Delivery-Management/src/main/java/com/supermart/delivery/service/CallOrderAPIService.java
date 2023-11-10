package com.supermart.delivery.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CallOrderAPIService {
    private final WebClient.Builder webClientBuilder;
    private final String orderMicroServiceUrl="http://ORDER-MANAGEMENT/api/order/";

    @CircuitBreaker(name = "order",fallbackMethod = "fallbackMethod")
    @Retry(name = "order")
    public boolean isOrderValid(String orderNumber) {
        String response = webClientBuilder.build()
                .get()
                .uri(orderMicroServiceUrl + "order-number/{order-number}", orderNumber)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response != null;
    }

    public boolean fallbackMethod(String orderNumber, RuntimeException runtimeException){
        return false;
    }
}
