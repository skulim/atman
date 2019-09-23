package com.intive.atman.service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountOperationException;

public interface TransferService {
    public AccountWithHistoryDTO getAccount(String accountNo) throws AccountOperationException;

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) throws AccountOperationException;

    public void initAccount(AccountDTO accountDTO);
}
