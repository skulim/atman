package com.intive.atman.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intive.atman.dto.AccountDTO;
import com.intive.atman.service.AccountService;

/**
 * Admin REST API for initiate accounts.
 *
 * @author michal.skulimowski
 *
 */
@RestController
@RequestMapping("/atman/admin")
public class RESTAdminService {

    private AccountService accountService;

	/**
	 * Injection constructor.
	 *
	 * @param componentService {@link ComponentService} instance
	 */
    @Autowired
    public RESTAdminService(AccountService accountService) {
        this.accountService = accountService;
	}

    @PostMapping(path = "/initAccounts")
    public void initAccounts(@Valid @RequestBody List<AccountDTO> accountDTO) {
        accountDTO.stream().forEach(accountService::initAccount);
    }
}
