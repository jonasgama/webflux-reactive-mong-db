package com.example.infra.gateway;

import com.example.infra.document.ItemCapped;
import com.example.infra.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class ItemReactiveGateway {

    @Autowired
    private ItemReactiveRepository repository;

    public Flux<ItemCapped> get(){
        return repository.findAllBy();
    }

}
