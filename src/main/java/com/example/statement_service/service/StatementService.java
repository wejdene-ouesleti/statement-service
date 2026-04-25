package com.example.statement_service.service;
import com.example.statement_service.client.T24Client;
import com.example.statement_service.entity.AccountSubscription;
import com.example.statement_service.entity.Statement;
import com.example.statement_service.entity.TransactionDTO;
import com.example.statement_service.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class StatementService {

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private T24Client t24Client;

    @Autowired
    private AccountSubscriptionService subscriptionService;
    private static final Logger logger = LoggerFactory.getLogger(StatementService.class);
    public Statement generateStatement(AccountSubscription acc) {
        String accountId = acc.getAccountId();
        String type = acc.getSubscriptionType();
        LocalDate nextExecutionDate = acc.getNextExecutionDate();

        LocalDate fromDate = getFromDate(type, nextExecutionDate);

        if (statementRepository.existsByAccountIdAndFromDateAndToDate(accountId, fromDate, nextExecutionDate)) {
            logger.warn("Statement already exists for this period");
            return null;
        }

        List<TransactionDTO> transactions =
                t24Client.getTransactions(accountId, fromDate, nextExecutionDate);

        logger.info("FromDate: " + fromDate + ", ToDate: " + nextExecutionDate + ", transactions found: " + transactions.size());

        double openingBalance = transactions.isEmpty()
                ? 0
                : transactions.get(0).getClosingBalance();

        double closingBalance = transactions.isEmpty()
                ? 0
                : transactions.get(transactions.size() - 1).getClosingBalance();

        double totalCredit = transactions.stream().mapToDouble(TransactionDTO::getCreditAmount).sum();
        double totalDebit = transactions.stream().mapToDouble(TransactionDTO::getDebitAmount).sum();

        Statement statement = new Statement();
        statement.setAccountId(accountId);
        statement.setGeneratedDate(LocalDate.now());
        statement.setFromDate(fromDate.atStartOfDay());
        statement.setToDate(nextExecutionDate.atStartOfDay());
        statement.setOpeningBalance(openingBalance);
        statement.setClosingBalance(closingBalance);
        statement.setTotalCredit(totalCredit);
        statement.setTotalDebit(totalDebit);
        statement.setTransactions(transactions);
        Statement saved = statementRepository.save(statement);
        logger.info("Statement saved for account: " + saved.getAccountId() + " at " + saved.getGeneratedDate());
        return saved;

    }



    public LocalDate calculateNextDate(String type, LocalDate today) {

        switch (type) {
            case "DAILY":
                return today.plusDays(1);
            case "WEEKLY":
                return today.plusWeeks(1);
            case "MONTHLY":
                return today.plusMonths(1);
            default:
                return today.plusDays(1);
        }
    }
    private LocalDate getFromDate(String type, LocalDate today) {
        switch (type) {
            case "DAILY": return today.minusDays(1);
            case "WEEKLY": return today.minusWeeks(1);
            case "MONTHLY": return today.minusMonths(1);
            default: return today.minusDays(1);
        }
    }


}