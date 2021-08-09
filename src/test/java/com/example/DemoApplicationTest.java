package com.example;

import com.example.infra.document.Item;
import com.example.infra.repository.ItemRespository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class DemoApplicationTest {

	@Autowired
	private ItemRespository repo;

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

}
