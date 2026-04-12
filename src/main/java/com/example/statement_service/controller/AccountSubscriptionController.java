package com.example.statement_service.controller;


import com.example.statement_service.entity.AccountSubscription;
import com.example.statement_service.service.AccountSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class AccountSubscriptionController {

    private final AccountSubscriptionService service;

    public AccountSubscriptionController(AccountSubscriptionService service) {
        this.service = service;
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<?> subscribe(@PathVariable String accountId) {
        return ResponseEntity.ok(
                service.subscribe(accountId, "MONTHLY")
        );
    }

    @GetMapping
    public List<AccountSubscription> getAllSubscribed() {
        return service.getSubscribedAccounts();
    }
    @PutMapping
    public AccountSubscription update(@RequestBody AccountSubscription sub) {
        return service.save(sub);
    }
}
