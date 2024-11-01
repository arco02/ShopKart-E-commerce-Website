package com.springspartans.shopkart.controller;

import com.springspartans.shopkart.model.Customer;
import com.springspartans.shopkart.service.CustomerService;
import com.springspartans.shopkart.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
	private ProductService productService;
    
    @GetMapping
	public String login() {
		return "customer/login";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "customer/signup";
	}
	
	@GetMapping("/update")
	public String updateinfo(Model model) {
		Customer customer = customerService.getCustomer();
        model.addAttribute("customer", customer);
        List<String> categoryList = productService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		return "customer/updateinfo";
	}
    
    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        boolean success = customerService.login(email, password);
        if (success) {
            return "redirect:/product";
        } else {
            return "redirect:/?msg=failed";
        }
    }
    
    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute Customer customer) {
        boolean success = customerService.signup(customer);
        if (success) {
            return "redirect:/";
        } else {
            return "redirect:/signup?msg=failed";
        }
    } 
    
    @PostMapping("/update")
    public String updateCustomer(@RequestParam String newName, @RequestParam long newPhone, 
    		@RequestParam String newAddress, @RequestParam String newPassword, @RequestParam String oldPassword, Model model) { 
    	boolean success = customerService.updateCustomer(newName, newPhone, newAddress, newPassword, oldPassword);
        if (success) {
        	Customer customer = customerService.getCustomer();
            model.addAttribute("customer", customer);
            return "redirect:/product";
        } else {
        	return "redirect:/update?msg=failed";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
    	customerService.logout();
    	return "customer/login";
    }
    
}
