package com.intive.atman.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountNotFoundException;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.LackOfFundsException;
import com.intive.atman.exception.TransactionOperationException;
import com.intive.atman.repository.AccountStorage;
import com.intive.atman.service.ValidatorService;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    private AccountStorage accountStorage;

    @Autowired
    public ValidatorServiceImpl(AccountStorage accountStorage) {
        this.accountStorage = accountStorage;
    }

    public void checkIfAccountExist(String accountNo) throws AccountNotFoundException {
        if (accountStorage.getAccount(accountNo) == null) {
            throw new AccountNotFoundException(String.format("Account with number %s not found", accountNo));
        }
    }

    public void checkIfAccountCouldBeCharged(String accountNo, BigDecimal amount) throws AccountOperationException {
        AccountDTO accountDTO = accountStorage.getAccount(accountNo);
        if (accountDTO == null) {
            throw new AccountNotFoundException(String.format("Account with number %s not found", accountNo));
        }

        if (accountDTO.getBalance().compareTo(amount) < 0) {
            throw new LackOfFundsException("Account doesn't have enought founds to charge");
        }
    }

    public void checkIfAccountsDiffer(TransactionDTO transactionDTO) throws TransactionOperationException {
        if (transactionDTO.getSourceAccountNo().equals(transactionDTO.getTargetAccountNo())) {
            throw new TransactionOperationException("Source and target account must be differ");
        }
    }

}
