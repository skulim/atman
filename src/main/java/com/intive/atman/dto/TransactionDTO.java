package com.intive.atman.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.intive.atman.enums.Currency;

public class TransactionDTO {

    @NotEmpty(message = "Please provide a source account number")
    private String sourceAccountNo;
    @NotEmpty(message = "Please provide a source account number")
    private String targetAccountNo;
    private Date transactionDate;

    @Min(value = 1, message = "Please provide amount of transaction greater then 1")
    private long amount;
    private Currency currency;

    public TransactionDTO() {
    }

    public TransactionDTO(TransactionDTO transactionDTO) {
        this.sourceAccountNo = transactionDTO.getSourceAccountNo();
        this.targetAccountNo = transactionDTO.getTargetAccountNo();
        this.transactionDate = transactionDTO.getTransactionDate();
        this.amount = transactionDTO.getAmount();
        this.currency = transactionDTO.getCurrency();
    }

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
