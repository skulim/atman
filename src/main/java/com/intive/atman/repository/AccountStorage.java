package com.intive.atman.repository;

import java.util.List;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.TransactionDTO;

public interface AccountStorage {

    /**
     * Returns account DTO for specific account number
     * 
     * @param accountNo
     *            account number
     * @return AccountDTO instance
     */
    public AccountDTO getAccount(String accountNo);

    public List<TransactionDTO> getAccountHistory(String accountNo);

    public boolean addTransaction(TransactionDTO transactionDTO);

    public void fundAccount(String accountNo, long amount);

    public void chargeAccount(String accountNo, long amount);
}
