package com.example.statement_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "statements")
public class Statement {

    @Id
    private String id;
    private String accountId;
    private LocalDate generatedDate;
    private LocalDateTime  fromDate;
    private LocalDateTime toDate;
    private double openingBalance;
    private double closingBalance;
    private double totalCredit;
    private double totalDebit;
    private List<TransactionDTO> transactions;
}