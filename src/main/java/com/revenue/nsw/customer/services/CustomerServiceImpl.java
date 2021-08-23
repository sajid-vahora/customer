package com.revenue.nsw.customer.services;

import com.revenue.nsw.customer.domain.Customer;
import com.revenue.nsw.customer.web.mappers.CustomerMapper;
import com.revenue.nsw.customer.web.model.CustomerDto;
import com.revenue.nsw.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDto> getAllCustomer() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Long> createCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        return customerRepository.save(customer)
                .map(Customer::getId);
    }

    @Override
    public Mono<Long> updateCustomer(Long id, CustomerDto customerDto) {
        final Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        return customerRepository.findById(id)
                .map(existingCustomer -> updateCustomer(customer, existingCustomer))
                .flatMap(customerRepository::save)
                .map(Customer::getId);
    }

    private Customer updateCustomer(Customer customer, Customer existingCustomer) {
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setOrganizationName(customer.getOrganizationName());
        existingCustomer.setPersonFlag(customer.getPersonFlag());
        existingCustomer.setDmsCustomerRecords(customer.getDmsCustomerRecords());
        existingCustomer.setFesCustomerRecords(customer.getFesCustomerRecords());
        existingCustomer.setImpsCustomerRecords(customer.getImpsCustomerRecords());
        existingCustomer.setTaxCustomerRecords(customer.getTaxCustomerRecords());
        existingCustomer.setTotalCustomerRecords(customer.getTotalCustomerRecords());
        existingCustomer.setAmountOwed(customer.getAmountOwed());
        existingCustomer.setAta(customer.getAta());
        existingCustomer.setUrl(customer.getUrl());
        return existingCustomer;
    }
}
