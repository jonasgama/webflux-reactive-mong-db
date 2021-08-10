package com.example.infra.repository;

import com.example.infra.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRespository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(String description);

}
