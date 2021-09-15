package com.example.demo.infra.controller;

import com.example.demo.app.domain.ItemClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {

    private WebClient web;

    public ItemController() {
        web = WebClient.create("http://localhost:8082");
    }

    @GetMapping("/client/items")
    public Flux<ItemClient> getItems() {
        return web.get().uri("/v2")
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToFlux(ItemClient.class)
            .log("getting all saved items");
    }


    @GetMapping("/client/items/{id}")
    public Mono<ItemClient> getItems(@PathVariable String id) {
        return web.get().uri("/v2/{id}", id)
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToMono(ItemClient.class);

    }

    @PostMapping("/client/items")
    public Mono<ItemClient> saveItem(@RequestBody ItemClient body) {
        return web.post().uri("/v2")
            .header("Content-Type", "application/json")
            .body(Mono.just(body), ItemClient.class)
            .retrieve()
            .bodyToMono(ItemClient.class)
            .log("new item has been created");
    }

    @PutMapping("/client/items/{id}")
    public Mono<ItemClient> updateItem(@PathVariable String id, @RequestBody ItemClient body) {
        return web.put().uri("/v2/{id}", id)
            .header("Content-Type", "application/json")
            .body(Mono.just(body), ItemClient.class)
            .retrieve()
            .bodyToMono(ItemClient.class)
            .log("new item has been updated");

    }


}
