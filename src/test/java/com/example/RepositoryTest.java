package com.example;

import com.example.infra.document.Item;
import com.example.infra.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class RepositoryTest {

	@Autowired
	private ItemRepository repo;

	@Before
	public void setup() {
		repo.deleteAll()
				.thenMany(repo.saveAll(bulk()))
				.doOnNext((bulked) -> System.out.println("Inserted data" + bulked))
				.blockLast();

	}

	private List<Item> bulk(){
		return Arrays.asList(
				new Item("A", "Computer", 2999.99),
				new Item("B", "Mouse", 14.59),
				new Item("C", "Keyboard", 77.54)
		);
	}

	@Test
	public void shouldGetBulkedItems(){
		StepVerifier.create(repo.findAll())
				.expectSubscription()
				.expectNextCount(3)
				.verifyComplete();
	}


	@Test
	public void shouldGetSpecificId(){
		Item item = repo.save(new Item(null, "TV", 1200.99)).block();
		StepVerifier.create(repo.findById(item.getId()))
				.expectSubscription()
				.expectNextMatches(record->	record.getId().equals(item.getId()))
				.verifyComplete();
	}

	@Test
	public void shouldGetByDescription(){
		repo.save(new Item(null, "TV", 1200.99)).block();
		repo.save(new Item(null, "TV", 1200.99)).block();
		repo.save(new Item(null, "TV", 1200.99)).block();
		StepVerifier.create(repo.findByDescription("TV"))
				.expectSubscription()
				.expectNextCount(3)
				.verifyComplete();
	}


	@Test
	public void shouldUpdateItem(){
		Double updatedValue = 229.99;
		repo.save(new Item(null, "Phone", 129.99)).block();
		Flux<Item> updatedPhone = repo.findByDescription("Phone")
				.map(item -> {
					item.setPrice(updatedValue);
					return item;
				})
				.flatMap(item -> repo.save(item));

		StepVerifier.create(updatedPhone)
				.expectSubscription()
				.expectNextMatches(record->	record.getPrice().equals(updatedValue))
				.verifyComplete();
	}


	@Test
	public void shouldDeleteItem(){
		String key = "REMOVE";
		repo.save(new Item(key, "Stereo", 99.99)).block();
		Mono<Void> deletedItem = repo.deleteById(key);

		StepVerifier.create(deletedItem)
				.expectSubscription()
				.verifyComplete();

		StepVerifier.create(repo.findById(key))
				.expectSubscription()
				.expectNextCount(0)
				.verifyComplete();
	}

}
