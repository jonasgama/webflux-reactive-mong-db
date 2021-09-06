package com.example.app.handler;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
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

}
