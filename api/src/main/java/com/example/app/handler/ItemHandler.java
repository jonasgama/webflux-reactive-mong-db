package com.example.app.handler;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler{

    @Autowired
    private ItemGateway gateway;

    public Mono<ServerResponse> getAll(ServerRequest request){
        return ServerResponse.ok().body(gateway.get(),Item.class);
    }

    public Mono<ServerResponse> get(ServerRequest request){
        String id = request.pathVariable("id");
        return ServerResponse.ok()
                            .body(gateway.get(id),Item.class)
                            .switchIfEmpty(
                                    ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> insert(ServerRequest request){
        return request
                .bodyToMono(Item.class)
                .flatMap(item -> gateway.save(item))
                .flatMap(result->ServerResponse.status(201).build());
    }

    public Mono<ServerResponse> remove(ServerRequest request){
        String id = request.pathVariable("id");
        return ServerResponse.ok().body(gateway.remove(id),Void.class);
    }


    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        if(id.isBlank()){
            throw new RuntimeException("null id");
        }
        return request
                .bodyToMono(Item.class)
                .flatMap(item -> gateway.get(id)
                        .map(toUpdate -> {
                            toUpdate.setDescription(item.getDescription());
                            toUpdate.setPrice(item.getPrice());
                            return toUpdate;
                        })).flatMap(updated -> gateway.save(updated))
                .flatMap(result->ServerResponse.noContent().build());
    }

}
