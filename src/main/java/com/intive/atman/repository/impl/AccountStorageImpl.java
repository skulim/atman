/**
 * 
 */
package com.intive.atman.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.entity.Account;
import com.intive.atman.entity.Transaction;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.repository.AccountStorage;

/**
 * @author michal.skulimowski
 *
 */
@Repository
public class AccountStorageImpl implements AccountStorage {

    // in milliseconds
    private static final long DEFAULT_TIMEOUT_OF_ACCOUNT_LOCK = 50;

    private Map<String, Account> accounts = new ConcurrentHashMap<>();
    private Map<String, List<Transaction>> history = new ConcurrentHashMap<>();
    
    @Override
    public void setAccount(AccountDTO accountDTO) {
        accounts.put(accountDTO.getNumber(), Account.fromDTO(accountDTO));
    }

    public AccountDTO getAccount(String accountNo) {
        Account account = accounts.get(accountNo);
        return account != null ? account.toDTO() : null;
    }

    public List<TransactionDTO> getAccountHistory(String accountNo) {
        List<Transaction> transaction = history.get(accountNo);
        return transaction == null 
                ? new ArrayList<>()
                : transaction.stream().map(Transaction::toDTO).collect(Collectors.toList());
    }

    public boolean addTransaction(String accountNo, TransactionDTO transactionDTO) {
        List<Transaction> transactions = history.get(accountNo);
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(Transaction.fromDTO(transactionDTO));
        history.put(accountNo, transactions);

        return true;
    }

    public void fundAccount(String accountNo, BigDecimal amount) {
        Account account = accounts.get(accountNo);
        account.setBalance(account.getBalance().add(amount));
        accounts.put(accountNo, account);
    }

    public void chargeAccount(String accountNo, BigDecimal amount) {
        Account account = accounts.get(accountNo);
        account.setBalance(account.getBalance().subtract(amount));
        accounts.put(accountNo, account);
    }

    @Override
    public boolean lockAccount(String accountNo) throws AccountOperationException {
        try {
            return accounts.get(accountNo).getLock().tryLock(DEFAULT_TIMEOUT_OF_ACCOUNT_LOCK, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new AccountOperationException("Can not get lock of account", e);
        }
    }

    @Override
    public void unlockAccount(String accountNo) {
        accounts.get(accountNo).getLock().unlock();
    }

}
