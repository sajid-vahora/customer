package com.revenue.nsw.customer.web.mappers;

import com.revenue.nsw.customer.domain.Customer;
import com.revenue.nsw.customer.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);
}
