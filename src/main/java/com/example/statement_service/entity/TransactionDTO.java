package com.example.statement_service.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TransactionDTO {
    private String transactionReference;
    private String debitCreditIndicator;
    private double debitAmount;
    private double creditAmount;
    private double closingBalance;
    private String valueDate;
    private String bookingDate;
    private String transactionDescription;
    private String beneficiaryAccountId;
    private String transactionCode;
    private String auditDateTime;
    private String paymentDetail;


}

