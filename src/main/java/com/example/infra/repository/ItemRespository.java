package com.example.infra.repository;

import com.example.infra.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemRespository extends ReactiveMongoRepository<Item, String> {
}
