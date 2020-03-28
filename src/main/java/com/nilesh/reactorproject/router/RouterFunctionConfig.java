package com.nilesh.reactorproject.router;

import com.nilesh.reactorproject.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<ServerResponse> route(
            SampleHandlerFunction sampleHandlerFunction)
    {
        return RouterFunctions.route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)),sampleHandlerFunction::flux)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)),sampleHandlerFunction::mono);

    }
}
