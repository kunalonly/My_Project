package com.shopme.admin.orders;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.admin.pagging.PagingAndSortingHelper;
import com.shopme.admin.pagging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Setting;

@Controller
public class OrderController {


	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

	private OrderService orderService;
	
	private SettingService settingService;
	
	@Autowired
	public OrderController(OrderService orderService, SettingService settingService) {
		super();
		this.orderService = orderService;
		this.settingService = settingService;
	}
	
	
	@GetMapping("/orders")
	public String listFirstPage() {
		
		return defaultRedirectURL;
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			HttpServletRequest request) {
		
		orderService.listByPage(1, helper);
		loadCurrencySetting(request);
		
		
		return "orders/orders";
	}


	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySetting=settingService.getCurrencySettings();
		
		for(Setting setting : currencySetting)
		{
			request.setAttribute(setting.getKey(),setting.getValue());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
}
