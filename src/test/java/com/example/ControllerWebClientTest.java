package com.example;

import com.example.infra.document.Item;
import com.example.infra.gateway.ItemGateway;
import com.example.infra.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
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
public class ControllerWebClientTest {

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

        client.get().uri("/v1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    public void shouldGetSpecificItem(){
    String key =  "KEY-TEST";
    String description = "Eye-Glasses";
     repo.save( new Item(key, description, 87.55)).block();


        client.get().uri("/v1/{id}", key)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
               .expectBody()
                .jsonPath("$.description", description);
    }


    @Test
    public void shouldNotFoundItem(){
        client.get().uri("/v1/{id}", "Inexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldCreateItem(){
        Item newItem = new Item("created-item", "nothing", 1.00);
        client.post().uri("/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange()
                .expectStatus().isCreated();
    }

    private List<Item> bulk(){
        return Arrays.asList(
                new Item("A", "Computer", 2999.99),
                new Item("B", "Mouse", 14.59),
                new Item("C", "Keyboard", 77.54)
        );
    }



}
