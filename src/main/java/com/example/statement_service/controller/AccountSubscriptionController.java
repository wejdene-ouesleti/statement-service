package com.example.statement_service.controller;


import com.example.statement_service.entity.AccountSubscription;
import com.example.statement_service.service.AccountSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Subscription API", description = "Subscription Management")
@RestController
@RequestMapping("/subscriptions")
public class AccountSubscriptionController {

    private final AccountSubscriptionService service;

    public AccountSubscriptionController(AccountSubscriptionService service) {
        this.service = service;
    }
    @Operation(summary = "Subscribe an account")
    @PostMapping("/{accountId}")
    public ResponseEntity<?> subscribe(@PathVariable String accountId ,  @RequestParam String type) {
        return ResponseEntity.ok(
                service.subscribe(accountId, type)
        );
    }
    @Operation(summary = "Get all subscribed accounts")
    @GetMapping
    public List<AccountSubscription> getAllSubscribed() {
        return service.getSubscribedAccounts();
    }
    @Operation(summary = "Update subscription")
    @PutMapping
    public AccountSubscription update(@RequestBody AccountSubscription sub) {
        return service.save(sub);
    }
}
