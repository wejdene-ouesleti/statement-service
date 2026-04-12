package com.example.statement_service.repository;

import com.example.statement_service.entity.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatementRepository extends MongoRepository<Statement, String> {
    List<Statement> findByAccountIdAndFromDateGreaterThanEqualAndToDateLessThanEqual(
            String accountId,
            LocalDate from,
            LocalDate to
    );
    boolean existsByAccountIdAndFromDateAndToDate(
            String accountId,
            LocalDate fromDate,
            LocalDate toDate
    );
}