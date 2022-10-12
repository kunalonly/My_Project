package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.utility.AuthenticationControllerHelperUtil;

@Controller
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService cartService;
@Autowired
	private AddressService addressService;
@Autowired
	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
@Autowired
private ShippingRateService rateService;
	
	
	
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request)  {
		
		
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		

		float estimatedTotal = 0.0F;

		for (CartItem item : cartItems) {
			
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;

		if (defaultAddress != null) {
			shippingRate = rateService.getShippingRateForAddress(defaultAddress);
		} else {
			usePrimaryAddressAsDefault = true;
			shippingRate = rateService.getShippingRateForCustomer(customer);
		}

		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		

		return "cart/shopping_cart";
	}
	
	
	
	
	
	
	
	
	
}
