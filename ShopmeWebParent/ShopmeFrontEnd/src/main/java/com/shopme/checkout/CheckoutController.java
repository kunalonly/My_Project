package com.shopme.checkout;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.utility.AuthenticationControllerHelperUtil;

@Controller
public class CheckoutController {

	@Autowired private OrderService orderService;
	
	@Autowired private CheckoutService checkoutService;
	@Autowired 	private AddressService addressService;
	@Autowired	private ShippingRateService shipService;
	@Autowired	private ShoppingCartService cartService;
	@Autowired	private CustomerService customerService;
	@Autowired private  SettingService settingService;
	@Autowired	private AuthenticationControllerHelperUtil authenticationControllerHelperUtil;
	
	
	
	
	
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			
			
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		
		if (shippingRate == null) {

			return "redirect:/cart";
		}

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		// Paypal
	//	String currencyCode = settingService.getCurrencyCode();
		//PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		//String paypalClientId = paymentSettings.getClientID();

	//	model.addAttribute("paypalClientId", paypalClientId);
		//model.addAttribute("currencyCode", currencyCode);
		
		//model.addAttribute("customer", customer);
		
		
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request)  {

		
String paymentType = request.getParameter("paymentMethod");
		
	
		
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);
	

		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			
			
		} else {
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		 orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		
		cartService.deleteByCustomer(customer);
		
		
		
		return "checkout/order_completed";
	}
	
}
