package com.example.infra.setup;

import com.example.infra.document.ItemCapped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class ItemCollectionSetup implements CommandLineRunner {

  @Autowired
  private ReactiveMongoOperations mongo;

  @Override
  public void run(String... args) throws Exception {
    createCappedCollection();
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
