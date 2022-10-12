package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.utility.AuthenticationControllerHelperUtil;


@RestController	
public class ShoppingCartRestController {

	

private ShoppingCartService cartService;
	
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	@Autowired
	public ShoppingCartRestController(ShoppingCartService cartService,
			CustomerService customerService,
			AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
		super();
		this.cartService = cartService;
		this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
	}


	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {

		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			
			Integer updatedQuantity;
			try {
				updatedQuantity = cartService.addProduct(productId, quantity, customer);
			} catch (ShoppingCartException ex) {
				// TODO Auto-generated catch block
				return ex.getMessage();
			}
			
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		}else {
			return "You must login to add this product to cart.";
		}
		
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
	
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		
		if(customer != null) {
			
			float subtotal = cartService.updateQuantity(productId, quantity, customer);

			return String.valueOf(subtotal);
		}else {
			return "You must login to change quantity of product.";
		}
		
	}
	
	
	
	
	
	
	
}
