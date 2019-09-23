package com.intive.atman.service;

import java.math.BigDecimal;

import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountNotFoundException;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.TransactionOperationException;

/**
 * Service with validation methods.
 * 
 * @author michal.skulimowski
 *
 */
public interface ValidatorService {
    void checkIfAccountExist(String accountNo) throws AccountNotFoundException;

    void checkIfAccountCouldBeCharged(String accountNo, BigDecimal amount) throws AccountOperationException;

    void checkIfAccountsDiffer(TransactionDTO transactionDTO) throws TransactionOperationException;
}
