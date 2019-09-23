/**
 * 
 */
package com.intive.atman.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.repository.AccountStorage;

/**
 * @author michal.skulimowski
 *
 */
@Repository
public class AccountStorageImpl implements AccountStorage {

    private Map<String, AccountDTO> accounts = new ConcurrentHashMap<>();
    private Map<String, List<TransactionDTO>> history = new ConcurrentHashMap<>();
    
    @Override
    public void setAccount(AccountDTO accountDTO) {
        accounts.put(accountDTO.getNumber(), accountDTO);
    }

    public AccountDTO getAccount(String accountNo) {
        return accounts.get(accountNo);
    }

    public List<TransactionDTO> getAccountHistory(String accountNo) {
        return history.get(accountNo);
    }

    public boolean addTransaction(String accountNo, TransactionDTO transactionDTO) {
        List<TransactionDTO> transactions = history.get(accountNo);
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transactionDTO);
        history.put(accountNo, transactions);

        return true;
    }

    public void fundAccount(String accountNo, long amount) {
        AccountDTO accountDTO = accounts.get(accountNo);
        accountDTO.setBalance(accountDTO.getBalance() + amount);
        accounts.put(accountNo, accountDTO);
    }

    public void chargeAccount(String accountNo, long amount) {
        AccountDTO accountDTO = accounts.get(accountNo);
        accountDTO.setBalance(accountDTO.getBalance() - amount);
        accounts.put(accountNo, accountDTO);
    }

}
