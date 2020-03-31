package com.assignment.bookstore.repository;

import com.assignment.bookstore.beans.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
