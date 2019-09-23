package com.intive.atman.repository;

import java.math.BigDecimal;
import java.util.List;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountOperationException;

/**
 * Storage of accounts and their history
 * 
 * @author michal.skulimowski
 */
public interface AccountStorage {

    public void setAccount(AccountDTO accountDTO);

    /**
     * Returns account DTO for specific account number
     * 
     * @param accountNo
     *            account number
     * @return AccountDTO instance
     */
    public AccountDTO getAccount(String accountNo);

    public boolean lockAccount(String accountNo) throws AccountOperationException;

    public void unlockAccount(String accountNo);

    public List<TransactionDTO> getAccountHistory(String accountNo);

    public boolean addTransaction(String accountNo, TransactionDTO transactionDTO);

    public void fundAccount(String accountNo, BigDecimal amount);

    public void chargeAccount(String accountNo, BigDecimal amount);
}
