package com.example;

import com.example.infra.document.Item;
import com.example.infra.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@DirtiesContext
public class RouteWebClientTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ItemRepository repo;


    @Before
    public void setup(){
        repo.deleteAll()
                .doOnNext(unused ->
                        System.out.println("database has been cleaned"))
                .block();
    }

    @Test
    public void shouldGetAllPersistedItems(){
        Flux.fromIterable(bulk())
                .flatMap(item -> repo.save(item))
                .blockLast();

        client.get().uri("/v2")
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    public void shouldGetSpecificItem(){
        String key =  "KEY-TEST-Functional";
        String description = "Cabinet";
        repo.save( new Item(key, description, 87.55)).block();


        client.get().uri("/v2/{id}", key)
                .header("Content-Type","application/json")
                .header("accept","application/json")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.description", description);
    }

    @Test
    public void shouldNotFoundItem(){
        client.get().uri("/v2/{id}", "Inexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldCreateItem(){
        Item newItem = new Item("route-created-item", "nothing", 1.00);
        client.post().uri("/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange()
                .expectStatus().isCreated();
    }


    @Test
    public void shouldDeleteSpecificItem(){
        String key =  "KEY-ROUTE-REMOVE";
        String description = "Router";
        repo.save( new Item(key, description, 87.55)).block();

        client.delete().uri("/v2/{id}", key)
                .header("Content-Type","application/json")
                .header("accept","application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        client.get().uri("/v2/{id}", key)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldHandleNotFoundItemToBeDeleted(){
        String key =  "KEY-NOT_FOUND";

        client.delete().uri("/v2/{id}", key)
                .header("Content-Type","application/json")
                .header("accept","application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        client.get().uri("/v2/{id}", key)
                .exchange()
                .expectStatus().isNotFound();
    }

    private List<Item> bulk(){
        return Arrays.asList(
                new Item("A", "Computer", 2999.99),
                new Item("B", "Mouse", 14.59),
                new Item("C", "Keyboard", 77.54)
        );
    }



}
