package com.intive.atman.service;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.exception.AccountOperationException;

/**
 * Service for get account with history of transaction and for initial accounts.
 * 
 * @author michal.skulimowski
 *
 */
public interface AccountService {
    AccountWithHistoryDTO getAccount(String accountNo) throws AccountOperationException;

    void initAccount(AccountDTO accountDTO);
}
