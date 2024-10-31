package com.springspartans.shopkart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springspartans.shopkart.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    
    @Query(value="SELECT * FROM customer WHERE name LIKE CONCAT(?1, '%')", nativeQuery=true)
    Optional<Customer> findByEmail(String email);
    
    

}