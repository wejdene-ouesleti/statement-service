package com.example.statement_service.repository;

import com.example.statement_service.entity.AccountSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface AccountSubscriptionRepository extends MongoRepository<AccountSubscription, String> {
    List<AccountSubscription> findBySubscribedTrue();
    List<AccountSubscription> findBySubscribedTrueAndNextExecutionDateLessThanEqual(LocalDate date);
}