package com.example.app.handler;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import com.example.infra.document.ItemCapped;
import com.example.infra.gateway.ItemReactiveGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemStreamHandler {

    @Autowired
    private ItemReactiveGateway gateway;

    public Mono<ServerResponse> getStreamAll(ServerRequest request){
        return ServerResponse.ok()
            .contentType(MediaType.valueOf("application/stream+json"))
            .body(gateway.get(), ItemCapped.class);
    }
}
