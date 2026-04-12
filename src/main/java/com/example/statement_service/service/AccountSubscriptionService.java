package com.example.statement_service.service;
import com.example.statement_service.entity.AccountSubscription;
import com.example.statement_service.repository.AccountSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class AccountSubscriptionService {
    private final AccountSubscriptionRepository repository;

    public AccountSubscriptionService(AccountSubscriptionRepository repository) {
        this.repository = repository;
    }

    public AccountSubscription subscribe(String accountId, String type) {
        AccountSubscription sub = new AccountSubscription();
        sub.setAccountId(accountId);
        sub.setSubscribed(true);
        sub.setActive(true);
        sub.setSubscriptionType(type);
        sub.setSubscriptionDate(LocalDate.now());
        sub.setNextExecutionDate(LocalDate.now());
        return repository.save(sub);
    }


    public List<AccountSubscription> getSubscribedAccounts() {
        // récupère tous les abonnés actifs
        return repository.findBySubscribedTrue();
    }

    public List<AccountSubscription> getAccountsToProcess(LocalDate today) {
        return repository.findBySubscribedTrueAndNextExecutionDateLessThanEqual(today);
    }

    public AccountSubscription save(AccountSubscription sub) {
        return repository.save(sub);
    }
}
