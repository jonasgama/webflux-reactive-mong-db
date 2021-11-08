package com.example.infra.controller;

import com.example.infra.document.ItemCapped;
import com.example.infra.gateway.ItemReactiveGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("stream/")
public class ItemStreamController {

    @Autowired
    private ItemReactiveGateway gateway;

    @GetMapping(value="v1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ItemCapped> getStream(){
        return gateway.get();
    }



}
