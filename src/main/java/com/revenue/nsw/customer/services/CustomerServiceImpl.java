package com.revenue.nsw.customer.services;

import com.revenue.nsw.customer.domain.Customer;
import com.revenue.nsw.customer.repositories.CustomerRepository;
import com.revenue.nsw.customer.web.mappers.CustomerMapper;
import com.revenue.nsw.customer.web.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final R2dbcEntityTemplate template;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDto> getAllCustomer(String name, boolean personFlag) {

        Query query;
        if (personFlag && !StringUtils.isEmpty(name)) {
            query = query(where("firstName").like(name).ignoreCase(true)
                    .and(where("personFlag").is(true)));
        } else if (!StringUtils.isEmpty(name)) {
            query = query(where("organizationName").like(name).ignoreCase(true)
                    .and(where("personFlag").is(false)));
        } else {
            query = Query.empty();
        }
        return template.select(Customer.class)
                .matching(query)
                .all()
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
