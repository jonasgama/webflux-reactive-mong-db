package com.example.app.handler;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.status;

@Component
public class ItemHandler{

    @Autowired
    private ItemGateway gateway;

    public Mono<ServerResponse> getAll(ServerRequest request){
        return ServerResponse.ok().body(gateway.get(),Item.class);
    }

    public Mono<ServerResponse> get(ServerRequest request){
        return request
                .bodyToMono(ServerRequest.class)
                        .map(serverRequest -> serverRequest.pathVariable("id"))
                        .flatMap(id ->
                                ServerResponse.ok()
                                        .body(gateway.get(id),Item.class)
                                        .switchIfEmpty(
                                                ServerResponse.notFound().build())
                        );
    }

    public Mono<ServerResponse> insert(ServerRequest request){
        return request
                .bodyToMono(Item.class)
                .flatMap(item -> gateway.save(item))
                .flatMap(result->ServerResponse.status(201).build());
    }

}
