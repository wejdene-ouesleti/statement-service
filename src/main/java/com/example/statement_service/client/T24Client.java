package com.example.statement_service.client;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import com.example.statement_service.entity.Account;
import com.example.statement_service.entity.TransactionDTO;

import java.time.LocalDate;
import java.util.List;

@Component
public class T24Client {

    private final RestTemplate restTemplate;

    public T24Client(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Account getAccount(String accountId) {
        return restTemplate.getForObject(
                "http://localhost:8081/t24/accounts/" + accountId,
                Account.class
        );
    }

    public List<TransactionDTO> getTransactions(String accountId, LocalDate from, LocalDate to) {

        String url = "http://localhost:8081/t24/accounts/" + accountId
                + "/transactions?fromDate=" + from + "&toDate=" + to;

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDTO>>() {}
        ).getBody();
    }}