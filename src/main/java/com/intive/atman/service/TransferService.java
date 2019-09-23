package com.intive.atman.service;

import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.TransactionOperationException;

/**
 * Service for transfer money between accounts.
 * 
 * @author michal.skulimowski
 *
 */
public interface TransferService {
    TransactionDTO transferMoney(TransactionDTO transactionDTO) throws AccountOperationException, TransactionOperationException;
}
