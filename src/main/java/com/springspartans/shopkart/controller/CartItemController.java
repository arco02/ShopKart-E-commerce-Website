package com.springspartans.shopkart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springspartans.shopkart.model.*;
import com.springspartans.shopkart.service.CartItemService;
import com.springspartans.shopkart.service.CustomerService;

@Controller
@RequestMapping("/cartitem")
public class CartItemController
{
	@Autowired
	private CartItemService cartservice;
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/cart")
	public String getAllCartItems(Model model)
	{
		List<CartItem> cart=cartservice.getAllCartItems();
		model.addAttribute("cart", cart);
		double totalPrice = cartservice.getCartPrice();
        	model.addAttribute("totalPrice", totalPrice);
        	Customer customer = customerService.getCustomer(); 
        	model.addAttribute("customer", customer);
   		return "/cartitem/cart";
	}
	@PostMapping("/addmore/{slno}")
	public String incrementQuantity(@PathVariable int slno)
	{
		cartservice.incrementQuantity(slno);
		return "redirect:/cartitem/cart";
	}
	@PostMapping("/delete/{slno}")
	public String deleteCartItem(@PathVariable int slno, Model model)
	{
		cartservice.deleteCartItem(slno);
		return "redirect:/cartitem/cart";
	}
	 @PostMapping("/add/{id}")
	 public String addToCart(@PathVariable int id)
	 {
		 Customer customer = customerService.getCustomer();
		 cartservice.addToCart(id,customer);
		 return "redirect:/cartitem/cart";
	 }
}
