package com.intive.atman.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.LockTimeoutException;
import com.intive.atman.exception.TransactionOperationException;
import com.intive.atman.repository.AccountStorage;
import com.intive.atman.service.TransferService;
import com.intive.atman.service.ValidatorService;

@Service
public class TransferServiceImpl implements TransferService {

    // in milliseconds
    private static final long DEFAULT_TIMEOUT_OF_INITTRANSACTION_LOCK = 50;

    private AccountStorage accountStorage;
    private ValidatorService validatorService;
    private Lock transactionLock = new ReentrantLock();

    @Autowired
    public TransferServiceImpl(AccountStorage accountStorage, ValidatorService validatorService) {
        this.accountStorage = accountStorage;
        this.validatorService = validatorService;
    }

    public AccountWithHistoryDTO getAccount(String accountNo) throws AccountOperationException {
        validatorService.checkIfAccountExist(accountNo);

        return new AccountWithHistoryDTO(accountStorage.getAccount(accountNo), accountStorage.getAccountHistory(accountNo));
    }

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) throws AccountOperationException, TransactionOperationException {
        validatorService.checkIfAccountExist(transactionDTO.getSourceAccountNo());
        validatorService.checkIfAccountExist(transactionDTO.getTargetAccountNo());
        validatorService.checkIfAccountsDiffer(transactionDTO);

        try {
            transactionLock.tryLock(DEFAULT_TIMEOUT_OF_INITTRANSACTION_LOCK, TimeUnit.MILLISECONDS);
            getAccountsLock(transactionDTO);
        } catch (InterruptedException e) {
            throw new TransactionOperationException("Can not get lock of inti transaction");
        } finally {
            transactionLock.unlock();
        }

        try {
            validatorService.checkIfAccountCouldBeCharged(transactionDTO.getSourceAccountNo(), transactionDTO.getAmount());
            
            transactionDTO.setTransactionDate(new Date());
            accountStorage.chargeAccount(transactionDTO.getSourceAccountNo(), transactionDTO.getAmount());
            accountStorage.fundAccount(transactionDTO.getTargetAccountNo(), transactionDTO.getAmount());

            // add fund transaction
            accountStorage.addTransaction(transactionDTO.getTargetAccountNo(), transactionDTO);

            // add charge transaction
            TransactionDTO chargeTransaction = generateChargeTransaction(transactionDTO);
            accountStorage.addTransaction(transactionDTO.getSourceAccountNo(), chargeTransaction);
            return chargeTransaction;
        } finally {
            releaseAccountsLock(transactionDTO);
        }
    }

    public void initAccount(AccountDTO accountDTO) {
        accountStorage.setAccount(accountDTO);
    }


    private TransactionDTO generateChargeTransaction(TransactionDTO transactionDTO) {
        TransactionDTO fundTransactionDTO = new TransactionDTO(transactionDTO);
        fundTransactionDTO.setAmount(fundTransactionDTO.getAmount().multiply(BigDecimal.valueOf(-1)));
        return fundTransactionDTO;
    }

    private void getAccountsLock(TransactionDTO transactionDTO) throws AccountOperationException {
        boolean isLockAcquires = accountStorage.lockAccount(transactionDTO.getSourceAccountNo());

        if (isLockAcquires) {
            if (!accountStorage.lockAccount(transactionDTO.getTargetAccountNo())) {
                isLockAcquires = false;
                accountStorage.unlockAccount(transactionDTO.getSourceAccountNo());
            }
        }

        if (!isLockAcquires) {
            throw new LockTimeoutException("Can not lock accounts");
        }
    }

    private void releaseAccountsLock(TransactionDTO transactionDTO) {
        accountStorage.unlockAccount(transactionDTO.getSourceAccountNo());
        accountStorage.unlockAccount(transactionDTO.getTargetAccountNo());
    }
}
