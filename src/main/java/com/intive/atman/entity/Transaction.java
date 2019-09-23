package com.intive.atman.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.enums.Currency;

public class Transaction {
    private String sourceAccountNo;
    private String targetAccountNo;
    private Date transactionDate;
    private BigDecimal amount;
    private Currency currency;

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    public String getTargetAccountNo() {
        return targetAccountNo;
    }

    public void setTargetAccountNo(String targetAccountNo) {
        this.targetAccountNo = targetAccountNo;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public TransactionDTO toDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccountNo(this.sourceAccountNo);
        transactionDTO.setTargetAccountNo(this.targetAccountNo);
        transactionDTO.setTransactionDate(this.transactionDate);
        transactionDTO.setAmount(this.amount);
        transactionDTO.setCurrency(this.currency);

        return transactionDTO;
    }


    public static Transaction fromDTO(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setSourceAccountNo(transactionDTO.getSourceAccountNo());
        transaction.setTargetAccountNo(transactionDTO.getTargetAccountNo());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setCurrency(transactionDTO.getCurrency());

        return transaction;
    }
}
