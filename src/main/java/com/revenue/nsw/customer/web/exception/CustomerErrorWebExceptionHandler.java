package com.revenue.nsw.customer.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class CustomerErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {


    public CustomerErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                            ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), serverRequest -> renderErrorResponse(serverRequest));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        Map<String, Object> errorAttributes = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        log.error("errorAttributes: " + errorAttributes);
        errorAttributes = getErrorAttributes(serverRequest, ErrorAttributeOptions.of(
                new ErrorAttributeOptions.Include[]{ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE}));
        log.error("errorAttributes: " + errorAttributes);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON).build();
                //.body(BodyInserters.fromValue(getError(serverRequest).getMessage()));
                //.body(BodyInserters.fromValue(errorAttributes.get("message")));
    }


}
