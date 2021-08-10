package com.example.infra.gateway;

import com.example.infra.document.Item;
import com.example.infra.repository.ItemRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;


@Component
public class ItemGateway {

    @Autowired
    private ItemRespository repository;

    public Flux<Item> get(){
        return repository.findAll();
    }

    public Mono<Void> remove(){
        return repository.deleteAll();
    }

    public Flux<Item> save(List<Item> items){
       return repository.saveAll(Flux.fromIterable(items));
    }

    public Flux<Item> getByDescription(String description){
        return repository.findByDescription(description);
    }

}
