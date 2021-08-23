package com.revenue.nsw.customer.web.handlers;

import com.revenue.nsw.customer.web.model.CustomerDto;
import com.revenue.nsw.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.revenue.nsw.customer.constants.CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomerHandler {

    private final CustomerService customerService;
    private static final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllCustomers(ServerRequest serverRequest) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.getAllCustomer(), CustomerDto.class);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest serverRequest) {
        final Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return customerService.getCustomerById(id)
                .flatMap(customerDto -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(customerDto))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerDto.class)
                .flatMap(customerDto -> customerService.createCustomer(customerDto))
                .flatMap(id -> created(fromPath(CUSTOMER_END_POINT_V1_BY_ID).build(id)).build());
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        final Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(CustomerDto.class)
                .flatMap(customerDto -> customerService.updateCustomer(id, customerDto))
                .flatMap(updatedCustomerDtoId -> {
                    return noContent().build();
                });
    }
}
