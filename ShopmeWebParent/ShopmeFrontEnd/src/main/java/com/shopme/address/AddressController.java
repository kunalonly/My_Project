package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.utility.AuthenticationControllerHelperUtil;

@Controller
public class AddressController {

private AddressService addressService;
	
	private CustomerService customerService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;

	@Autowired
	public AddressController(AddressService addressService, 
			                 CustomerService customerService,
			                 AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		super();
		this.addressService = addressService;
		this.customerService = customerService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}
	
	
	
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		

		boolean usePrimaryAddressAsDefault = true;
		
		for (Address address : listAddresses) {
			
			if (address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}
		
		
		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}
		

@GetMapping("/address_book/default/{id}")
public String setDefaultAddress(@PathVariable("id") Integer addressId,
		HttpServletRequest request) {
	
	Customer customer  = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
	

	addressService.setDefaultAddress(addressId, customer.getId());

	String redirectOption = request.getParameter("redirect");
	String redirectURL = "redirect:/address_book";

	if ("cart".equals(redirectOption)) {
		redirectURL = "redirect:/cart";
	}else if ("checkout".equals(redirectOption)) {
		redirectURL = "redirect:/checkout";
	
		
		}		

	return redirectURL; 

}
	
}
