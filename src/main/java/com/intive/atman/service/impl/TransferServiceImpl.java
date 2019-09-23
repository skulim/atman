package com.intive.atman.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountNotFoundException;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.LackOfFundsException;
import com.intive.atman.exception.LockTimeoutException;
import com.intive.atman.repository.AccountStorage;
import com.intive.atman.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

    // in milliseconds
    private static final long DEFAULT_TIMEOUT_OF_ACCOUNT_LOCK = 10;

    @Autowired
    private AccountStorage accountStorage;

    public AccountWithHistoryDTO getAccount(String accountNo) throws AccountOperationException {
        checkIfAccountExist(accountNo);

        return new AccountWithHistoryDTO(accountStorage.getAccount(accountNo), accountStorage.getAccountHistory(accountNo));
    }

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) throws AccountOperationException {
        checkIfAccountExistAndCouldBeCharged(transactionDTO.getSourceAccountNo(), transactionDTO.getAmount());
        checkIfAccountExist(transactionDTO.getTargetAccountNo());

        getAccountLocks(transactionDTO);

        try {
            transactionDTO.setTransactionDate(new Date());
            accountStorage.chargeAccount(transactionDTO.getSourceAccountNo(), transactionDTO.getAmount());
            accountStorage.fundAccount(transactionDTO.getTargetAccountNo(), transactionDTO.getAmount());

            // add fund transaction
            accountStorage.addTransaction(transactionDTO);

            // add charge transaction
            TransactionDTO chargeTransaction = generateChargeTransaction(transactionDTO);
            accountStorage.addTransaction(chargeTransaction);

            return chargeTransaction;
        } finally {
            releaseAccountLocks(transactionDTO);
        }
    }

    private void checkIfAccountExist(String accountNo) throws AccountNotFoundException {
        if (accountStorage.getAccount(accountNo) == null) {
            throw new AccountNotFoundException(String.format("Account with number %s not found", accountNo));
        }
    }

    private void checkIfAccountExistAndCouldBeCharged(String accountNo, long amount) throws AccountOperationException {
        AccountDTO accountDTO = accountStorage.getAccount(accountNo);
        if (accountDTO == null) {
            throw new AccountNotFoundException(String.format("Account with number %s not found", accountNo));
        }

        if (accountDTO.getBalance() < amount) {
            throw new LackOfFundsException("Account doesn't have enought founds to charge");
        }
    }

    private TransactionDTO generateChargeTransaction(TransactionDTO transactionDTO) {
        TransactionDTO fundTransactionDTO = new TransactionDTO(transactionDTO);
        fundTransactionDTO.setAmount(-1 * fundTransactionDTO.getAmount());
        return fundTransactionDTO;
    }

    private void getAccountLocks(TransactionDTO transactionDTO) throws AccountOperationException {
        boolean isLockAcquires = false;
        try {
            isLockAcquires = accountStorage.getAccount(transactionDTO.getSourceAccountNo()).getLock().tryLock(DEFAULT_TIMEOUT_OF_ACCOUNT_LOCK,
                    TimeUnit.MILLISECONDS);

            if (isLockAcquires) {
                if (!accountStorage.getAccount(transactionDTO.getTargetAccountNo()).getLock().tryLock(DEFAULT_TIMEOUT_OF_ACCOUNT_LOCK,
                    TimeUnit.MILLISECONDS)) {
                    isLockAcquires = false;
                    accountStorage.getAccount(transactionDTO.getSourceAccountNo()).getLock().unlock();
                }
            }
        } catch (InterruptedException iEx) {
            throw new LockTimeoutException("Can not lock accounts", iEx);
        }

        if (!isLockAcquires) {
            throw new LockTimeoutException("Can not lock accounts");
        }
    }

    private void releaseAccountLocks(TransactionDTO transactionDTO) {
        accountStorage.getAccount(transactionDTO.getSourceAccountNo()).getLock().unlock();
        accountStorage.getAccount(transactionDTO.getTargetAccountNo()).getLock().unlock();
    }
}
