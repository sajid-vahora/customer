package com.revenue.nsw.customer.web.router;

import com.revenue.nsw.customer.constants.CustomerConstant;
import com.revenue.nsw.customer.web.handlers.CustomerHandler;
import com.revenue.nsw.customer.web.model.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouter {


    @RouterOperations({
            @RouterOperation(
                    path = CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID,
                    method = RequestMethod.GET,
                    beanClass = CustomerHandler.class,
                    beanMethod = "getCustomerById",
                    operation = @Operation(
                            operationId = "customer.getAll",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(
                                            schema = @Schema(implementation = CustomerDto.class))),
                                    @ApiResponse(responseCode = "404", description = "NotFound"),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
                    )),
            @RouterOperation(
                    path = CustomerConstant.CUSTOMER_END_POINT_V1,
                    method = RequestMethod.GET,
                    beanClass = CustomerHandler.class,
                    beanMethod = "listCustomers",
                    operation = @Operation(
                            operationId = "customer.getById",
                            parameters = {
                                    @Parameter(name = "name", in = ParameterIn.QUERY, required = true, description = "First name of the customer or organization"),
                                    @Parameter(name = "personFlag", in = ParameterIn.QUERY, description = "Set to true to search individual customer and false to search organization")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(
                                            array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class)))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
                    )),
            @RouterOperation(
                    path = CustomerConstant.CUSTOMER_END_POINT_V1,
                    method = RequestMethod.POST,
                    beanClass = CustomerHandler.class,
                    beanMethod = "createCustomer",
                    operation = @Operation(
                            operationId = "customer.create",
                            requestBody = @RequestBody(
                                    required = true, description = "Enter Request body as Json Object",
                                    content = @Content(
                                            schema = @Schema(implementation = CustomerDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Created", headers = {
                                            @Header(name = "Location",
                                                    description = "uri pointing to newly created customer",
                                                    schema = @Schema(implementation = String.class))}),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
                            }
                    )),
            @RouterOperation(
                    path = CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID,
                    method = RequestMethod.PUT,
                    beanClass = CustomerHandler.class,
                    beanMethod = "updateCustomer",
                    operation = @Operation(
                            operationId = "customer.update",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            requestBody = @RequestBody(
                                    required = true, description = "Enter Request body as Json Object",
                                    content = @Content(
                                            schema = @Schema(implementation = CustomerDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "No Content"),
                                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                                    @ApiResponse(responseCode = "404", description = "Not Found"),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
                    ))})

    @Bean
    public RouterFunction<ServerResponse> customerRoute(CustomerHandler customerHandler) {
        return RouterFunctions
                .route(GET(CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID).and(accept(MediaType.APPLICATION_JSON)),
                        customerHandler::getCustomerById)
                .andRoute(GET(CustomerConstant.CUSTOMER_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON)),
                        customerHandler::listCustomers)
                .andRoute(POST(CustomerConstant.CUSTOMER_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON)),
                        customerHandler::createCustomer)
                .andRoute(PUT(CustomerConstant.CUSTOMER_END_POINT_V1_BY_ID).and(accept(MediaType.APPLICATION_JSON)),
                        customerHandler::updateCustomer);
    }
}
