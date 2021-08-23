package com.revenue.nsw.customer.repositories;

import com.revenue.nsw.customer.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

}
