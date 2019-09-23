package com.intive.atman.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.repository.AccountStorage;
import com.intive.atman.service.AccountService;
import com.intive.atman.service.ValidatorService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountStorage accountStorage;
    private ValidatorService validatorService;

    @Autowired
    public AccountServiceImpl(AccountStorage accountStorage, ValidatorService validatorService) {
        this.accountStorage = accountStorage;
        this.validatorService = validatorService;
    }

    public AccountWithHistoryDTO getAccount(String accountNo) throws AccountOperationException {
        validatorService.checkIfAccountExist(accountNo);

        return new AccountWithHistoryDTO(accountStorage.getAccount(accountNo), accountStorage.getAccountHistory(accountNo));
    }

    public void initAccount(AccountDTO accountDTO) {
        accountStorage.setAccount(accountDTO);
    }
}
