package com.intive.atman.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for money transfer beetween accounts
 *
 * @author skulim
 *
 */
@RestController
public class RESTTransferService {

	private ComponentService componentService;

	/**
	 * Injection constructor.
	 *
	 * @param componentService {@link ComponentService} instance
	 */
    @Autowired
	public RESTTransferService(ComponentService componentService) {
		this.componentService = componentService;
	}

	/**
	 * Creates instance of {@link ComponentDTO}. Internally delegates action to PENG to store
	 * {@link ProductDTO} instance. Note that PENG validation is fired on the API invocation.
	 *
	 * @param component data of new {@link ComponentDTO} to be created
	 *
	 * @return created instance of {@link ComponentDTO}
	 *
	 * @see ProductService
	 */
    @PostMapping("/employees")
	public ComponentDTO create(@Valid ComponentDTO component) {
		return componentService.create(component);
	}

}
