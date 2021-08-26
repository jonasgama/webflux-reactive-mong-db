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
    public Mono<ResponseEntity<Object>> get(@PathVariable String id){
        return gateway.get(id)
                .map(item -> ok(item))
                .defaultIfEmpty(notFound());
    }

    @DeleteMapping("v1/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> delete(@PathVariable String id){
        return gateway.remove(id);
    }

    @PostMapping("v1")
    public Mono<ResponseEntity<Object>> save(@RequestBody Item body){
        return gateway.save(body)
                .map(item -> created(item));
    }

    private ResponseEntity<Object> notFound(){
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Object> ok(Object body){
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Object> removed(){
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Object> created(Object body){
        return ResponseEntity.created(null).build();
    }


}
