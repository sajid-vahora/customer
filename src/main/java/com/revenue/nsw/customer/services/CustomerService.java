package com.revenue.nsw.customer.services;

import com.revenue.nsw.customer.web.model.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDto> getAllCustomer();

    Mono<CustomerDto> getCustomerById(Long id);

    Mono<Long> createCustomer(CustomerDto customerDto);

    Mono<Long> updateCustomer(Long id, CustomerDto customerDto);
}
