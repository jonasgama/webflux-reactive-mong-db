package com.example.infra.setup;

import com.example.infra.document.ItemCapped;
import com.example.infra.repository.ItemReactiveRepository;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Profile("!test")
public class ItemCollectionSetup implements CommandLineRunner {

  @Autowired
  private ReactiveMongoOperations mongo;

  @Autowired
  private ItemReactiveRepository repo;

  @Override
  public void run(String... args) throws Exception {
    Mono<Void> collectionCreated = createCappedCollection();
    infiniteCappedInsertion(collectionCreated);

  }

  private void infiniteCappedInsertion(
      Mono<Void> collectionCreated) {
    collectionCreated
        .doFirst(repo::deleteAll)
        .thenMany(Flux.interval(Duration.ofSeconds(1)))
        .map(i -> new ItemCapped(null, "Item Capped Description" + i, 100.0 + 1.1 * i))
        .flatMap(repo::insert)
        .subscribe(item -> System.out.println("Inserted item is:"+ item));
  }

  private Mono<Void> createCappedCollection(){
    return mongo.dropCollection(ItemCapped.class)
        .then(mongo
            .createCollection(ItemCapped.class, CollectionOptions.empty()
                .maxDocuments(20).size(50000).capped()).log("createCappedCollection")
        )
        .then(Mono.empty());
  }
}
