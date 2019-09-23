package com.intive.atman.entity;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.enums.Currency;

public class Account {
    private String accountNo;
    private String name;
    private BigDecimal balance;
    private Currency currency;
    private Lock lock = new ReentrantLock();

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Lock getLock() {
        return lock;
    }

    public AccountDTO toDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber(accountNo);
        accountDTO.setName(name);
        accountDTO.setBalance(balance);
        accountDTO.setCurrency(currency);

        return accountDTO;
    }

    public static Account fromDTO(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountNo(accountDTO.getNumber());
        account.setName(accountDTO.getName());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());

        return account;
    }
}
