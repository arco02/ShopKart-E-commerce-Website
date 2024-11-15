package com.springspartans.shopkart.service;

import com.springspartans.shopkart.model.Customer;
import com.springspartans.shopkart.repository.CustomerRepository;
import com.springspartans.shopkart.util.ImageUploadValidator;
import com.springspartans.shopkart.util.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class CustomerService {

	private final String projectPath = "<your_project_path>";
	private final String uploadPath = projectPath + "\\src\\main\\resources\\static\\images\\customer";
	
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageUploadValidator imageUploadValidator;

    @PostConstruct
    void addDemoUser() {
        if (customerRepository.findByEmail("demo@springspartans.com").isEmpty()) {
            Customer demoUser  = new Customer(0, "Demo User", "demo@springspartans.com", passwordEncoder.encode("shopkart123"), "JD Block, Sector III, Salt Lake City, Kolkata-700106", 9876543210L, "user1.jpg");
            customerRepository.save(demoUser);
        }
    }

    public boolean login(String email, String password) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
            httpSession.setAttribute("loggedInCustomer", customer.get());
            return true;
        }
        return false;
    }

    public boolean signup(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            return false;
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return true;
    }

    public Customer getCustomer() {
        return (Customer) httpSession.getAttribute("loggedInCustomer");
    }

    public boolean updateCustomer(
    	String newName, long newPhone, String newAddress, 
    	String newPassword, String oldPassword, MultipartFile profilePicture
    ) throws IOException {
        Customer loggedInCustomer = (Customer) httpSession.getAttribute("loggedInCustomer");
        if (loggedInCustomer != null && passwordEncoder.matches(oldPassword, loggedInCustomer.getPassword())) {
            String encodedPassword = newPassword.isEmpty() ? loggedInCustomer.getPassword() : passwordEncoder.encode(newPassword);
            String profilePictureName = profilePicture != null ? "user" + loggedInCustomer.getId() + ".jpg" : null;
            Customer updatedCustomer = new Customer(loggedInCustomer.getId(), newName, loggedInCustomer.getEmail(), encodedPassword, newAddress, newPhone, profilePictureName);
            customerRepository.save(updatedCustomer);
            httpSession.setAttribute("loggedInCustomer", updatedCustomer);
            
            if (imageUploadValidator.isValidImage(profilePicture)) {
                File destination = new File(uploadPath);
                if (!destination.exists()) {
                	destination.mkdirs();
                }
                File fileToSave = new File(destination, profilePictureName);
                profilePicture.transferTo(fileToSave);
            }  else if (profilePicture != null && !profilePicture.isEmpty()) {
            	throw new IllegalArgumentException("Improper file format!");
            }
            return true;
        }
        return false;
    }

    public void logout() {
        httpSession.invalidate();
    }
    
}
