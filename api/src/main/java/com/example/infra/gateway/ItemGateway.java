package com.example.infra.gateway;

import com.example.infra.document.Item;
import com.example.infra.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class ItemGateway {

    @Autowired
    private ItemRepository repository;

    public Flux<Item> get(){
        return repository.findAll();
    }

    public Mono<Item> get(String id){
        return repository.findById(id);
    }

    public Mono<Void> remove(){
        return repository.deleteAll();
    }

    public Mono<Void> remove(String id){
        return repository.deleteById(id);
    }

    public Flux<Item> save(List<Item> items){
       return repository.saveAll(Flux.fromIterable(items));
    }

    public Mono<Item> save(Item item){
       return repository.save(item);
    }

    public Flux<Item> getByDescription(String description){
        return repository.findByDescription(description);
    }
}
