package com.intive.atman.dto;

import java.util.List;

public class AccountWithHistoryDTO {

    private AccountDTO accountDTO;
    private List<TransactionDTO> transactions;

    public AccountWithHistoryDTO(AccountDTO accountDTO, List<TransactionDTO> transactions) {
        this.accountDTO = accountDTO;
        this.transactions = transactions;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
