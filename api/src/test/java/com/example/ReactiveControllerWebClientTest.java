package com.example;

import com.example.infra.document.Item;
import com.example.infra.document.ItemCapped;
import com.example.infra.repository.ItemReactiveRepository;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
public class ReactiveControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ItemReactiveRepository repo;

    @Autowired
    private ReactiveMongoOperations mongo;


    @Before
    public void setup(){
        Mono<Void> collectionCreated = createCappedCollection();
        finiteCappedInsertion(collectionCreated);
    }

    private void finiteCappedInsertion(Mono<Void> collectionCreated){
        Flux<ItemCapped> take = Flux.interval(Duration.ofMillis(1))
            .map(i -> new ItemCapped(null, "Item Capped Description" + i, 100.0 + 1.1 * i))
            .take(5);

        collectionCreated
            .thenMany(take)
            .flatMap(repo::insert)
            .doOnNext(item -> System.out.println("Inserted item is:"+ item))
            .blockLast();
    }

    private Mono<Void> createCappedCollection(){
        return mongo.dropCollection(ItemCapped.class)
            .then(mongo
                .createCollection(ItemCapped.class, CollectionOptions.empty()
                    .maxDocuments(20).size(50000).capped()).log("createCappedCollection")
            )
            .then(Mono.empty());
    }

    @Test
    public void shouldGetAllPersistedItems(){
        Flux<ItemCapped> responseBody = client.get().uri("/stream/v1")
            .exchange()
            .expectStatus().isOk()
            .returnResult(ItemCapped.class)
            .getResponseBody()
                .take(5);

        StepVerifier.create(responseBody)
            .expectNextCount(5)
            .thenCancel()
            .verify();
    }
}
