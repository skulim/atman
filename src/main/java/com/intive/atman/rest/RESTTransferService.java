package com.intive.atman.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.intive.atman.dto.AccountWithHistoryDTO;
import com.intive.atman.dto.TransactionDTO;
import com.intive.atman.exception.AccountNotFoundException;
import com.intive.atman.exception.AccountOperationException;
import com.intive.atman.exception.TransactionOperationException;
import com.intive.atman.service.AccountService;
import com.intive.atman.service.TransferService;

/**
 * REST API for money transfer between accounts
 *
 * @author michal.skulimowski
 *
 */
@RestController
@RequestMapping("/atman")
public class RESTTransferService {

    private TransferService transferService;
    private AccountService accountService;

	/**
	 * Injection constructor.
	 *
	 * @param componentService {@link ComponentService} instance
	 */
    @Autowired
    public RESTTransferService(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
	}

    @GetMapping(path = "/account/{accountNo}")
    public AccountWithHistoryDTO getAccount(@PathVariable("accountNo") @NotEmpty String accountNo) {
        try {
            return accountService.getAccount(accountNo);
        } catch (AccountOperationException ex) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (ex instanceof AccountNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, ex.getMessage(), ex);
        }
    }

    @PostMapping(path = "/transfer")
    public TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO) {
        try {
            return transferService.transferMoney(transactionDTO);
        } catch (AccountOperationException | TransactionOperationException ex) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if( ex instanceof AccountNotFoundException ) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, ex.getMessage(), ex);
        }
	}
}
