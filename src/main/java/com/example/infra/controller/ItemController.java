package com.example.infra.controller;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class ItemController {

    @Autowired
    private ItemGateway gateway;

    @GetMapping("v1")
    public Flux<Item> getAllItems(){
        return gateway.get();
    }

    @GetMapping("v1/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id){
        return gateway.get(id)
                .map(item -> ok(item))
                .defaultIfEmpty(notFound());
    }

    @PostMapping("v1")
    public Mono<ResponseEntity<Item>> saveItem(@RequestBody Item body){
        return gateway.save(body)
                .map(item -> created(item));
    }

    private ResponseEntity<Item> notFound(){
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Item> ok(Object body){
        return new ResponseEntity(body, HttpStatus.OK);
    }

    private ResponseEntity<Item> created(Object body){
        return new ResponseEntity(body, HttpStatus.CREATED);
    }


}
