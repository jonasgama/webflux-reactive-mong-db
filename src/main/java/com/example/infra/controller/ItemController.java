package com.example.infra.controller;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
public class ItemController {

    @Autowired
    private ItemGateway gateway;

    @GetMapping("v1")
    public Flux<Item> getAllItems(){
        return gateway.get();
    }


}
