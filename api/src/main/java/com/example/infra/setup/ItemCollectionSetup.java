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

@Component
@Profile("!test")
public class ItemCollectionSetup implements CommandLineRunner {

  @Autowired
  private ReactiveMongoOperations mongo;

  @Autowired
  private ItemReactiveRepository repo;

  @Override
  public void run(String... args) throws Exception {
    createCappedCollection();
    infiniteCappedInsertion();

  }

  private void infiniteCappedInsertion() {
    repo.deleteAll();
    repo.insert(  Flux.interval(Duration.ofSeconds(2))
          .map(i -> new ItemCapped(null, "ITEM", 1.0+i)) )
        .subscribe(newItem -> System.out.println(newItem));
  }

  private void createCappedCollection(){
    mongo.dropCollection(ItemCapped.class);
    mongo.createCollection(ItemCapped.class, CollectionOptions
        .empty()
        .maxDocuments(20)
        .size(50000)
        .capped()
    );
  }
}
