package com.revenue.nsw.customer.webclient;

import com.revenue.nsw.customer.constants.CustomerConstant;
import com.revenue.nsw.customer.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static com.revenue.nsw.customer.constants.CustomerConstant.CUSTOMER_END_POINT_V1;
import static com.revenue.nsw.customer.constants.CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class CustomerWebClientTest {

    @Autowired
    WebTestClient webTestClient;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = CustomerDto.builder()
                .personFlag(true)
                .firstName("test11")
                .organizationName("")
                .totalCustomerRecords(40)
                .amountOwed(10000.3)
                .impsCustomerRecords(10)
                .fesCustomerRecords(10)
                .dmsCustomerRecords(10)
                .taxCustomerRecords(10)
                .ata(true)
                .url("")
                .build();
    }

    @Test
    void getAllCustomers() {
        webTestClient.post()
                .uri(CustomerConstant.CUSTOMER_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(customerDto))
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_END_POINT_V1).build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .consumeWith(result -> assertThat(result.getResponseBody().size()).isNotZero());
    }

    @Test
    void getCustomerById() {
        webTestClient.post()
                .uri(CUSTOMER_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(customerDto))
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_END_POINT_V1_BY_ID).build(1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CustomerDto.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody().getFirstName()).isEqualTo("test11");
                });
    }

    @Test
    void getCustomerByIdNotFound() {

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_END_POINT_V1_BY_ID).build(UUID.randomUUID().hashCode()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createCustomer() {

        webTestClient.post()
                .uri(CustomerConstant.CUSTOMER_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(customerDto))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void updateCustomer() {

        final Long id = Long.valueOf(1);

        webTestClient.post()
                .uri(CustomerConstant.CUSTOMER_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(customerDto))
                .exchange()
                .expectStatus().isCreated();

        CustomerDto updateCustomerDto = CustomerDto.builder().firstName("testUser").build();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_END_POINT_V1_BY_ID).build(id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(updateCustomerDto))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_END_POINT_V1_BY_ID).build(id))
                .exchange()
                .expectBody(CustomerDto.class)
                .consumeWith(result -> assertThat(result.getResponseBody().getFirstName()).isEqualTo("testUser"));
    }
}