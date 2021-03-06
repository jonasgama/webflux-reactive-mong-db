package com.example.infra.route;

import com.example.app.handler.ItemHandler;
import com.example.app.handler.ItemStreamHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ItemRoute {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ItemHandler handler){
        return route(GET("/v2")
                        .and(contentType(APPLICATION_JSON)),
                        handler::getAll)
                .andRoute(GET("/v2/{id}")
                                .and(contentType(APPLICATION_JSON)),
                        handler::get)
                .andRoute(POST("/v2")
                                .and(contentType(APPLICATION_JSON)),
                        handler::insert)
                .andRoute(DELETE("/v2/{id}")
                                        .and(contentType(APPLICATION_JSON)),
                        handler::remove)
                .andRoute(PUT("/v2/{id}")
                                        .and(contentType(APPLICATION_JSON)),
                        handler::update);

    }

    @Bean
    public RouterFunction<ServerResponse> routerStreamFunction(ItemStreamHandler handler){
        return route(GET("/stream/v2"),
            handler::getStreamAll);
    }
}
