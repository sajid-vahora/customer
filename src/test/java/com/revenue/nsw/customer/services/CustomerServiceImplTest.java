package com.revenue.nsw.customer.services;

import com.revenue.nsw.customer.repositories.CustomerRepository;
import com.revenue.nsw.customer.web.mappers.CustomerMapper;
import com.revenue.nsw.customer.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class CustomerServiceImplTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    @Autowired
    private CustomerMapper customerMapper;

    private CustomerServiceImpl customerService;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        this.customerRepository.deleteAll().subscribe();
        this.customerService = new CustomerServiceImpl(customerRepository, template, customerMapper);
        this.customerDto = CustomerDto.builder()
                .personFlag(true)
                .firstName("test11")
                .organizationName("")
                .ata(true)
                .url("")
                .build();
    }

    @Test
    void testGetAllCustomer() {

        Flux<CustomerDto> customerDtoFlux = customerService.createCustomer(customerDto)
                .flatMapMany(customerDto1 -> customerService.getAllCustomer("test11", true));
        StepVerifier.create(customerDtoFlux)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetCustomerById() {
        Mono<Long> customerMono = customerService.createCustomer(customerDto);
        Mono<CustomerDto> customerDtoMono = customerMono
                .flatMap(id -> customerService.getCustomerById(id));
        customerDtoMono.subscribe(customerDto -> {
            assertThat(customerDto).isNotNull();
            assertThat(customerDto.getFirstName()).isEqualTo("test11");
            assertThat(customerDto.getPersonFlag()).isTrue();
            assertThat(customerDto.getOrganizationName()).isEqualTo("");
            assertThat(customerDto.getAta()).isTrue();
        });
    }

    @Test
    void testGetCustomerByIdNotFound() {
        Mono<CustomerDto> customerDtoMono = customerService.getCustomerById((long) UUID.randomUUID().hashCode());
        StepVerifier.create(customerDtoMono)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testCreateCustomer() {
        Mono<Long> customerMono = customerService.createCustomer(customerDto);
        StepVerifier.create(customerMono)
                .expectSubscription()
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    void testUpdateCustomer() {
        Mono<Long> customerMono = customerService.createCustomer(customerDto);
        CustomerDto  customerDtoToBeUpdated = CustomerDto.builder()
                .firstName("")
                .personFlag(false)
                .organizationName("org1")
                .impsCustomerRecords(11)
                .dmsCustomerRecords(11)
                .fesCustomerRecords(11)
                .taxCustomerRecords(11)
                .totalCustomerRecords(44)
                .ata(false)
                .amountOwed(22.55)
                .build();
        Mono<Long> voidMono = customerMono
                .flatMap(id -> customerService.updateCustomer(id, customerDtoToBeUpdated));

        StepVerifier.create(voidMono)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }
}