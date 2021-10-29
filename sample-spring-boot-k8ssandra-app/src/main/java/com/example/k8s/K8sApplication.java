package com.example.k8s;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication
public class K8sApplication {

	public static void main(String[] args) {
		SpringApplication.run(K8sApplication.class, args);
	}


	@Bean
	ApplicationRunner applicationRunner(CustomerRepository repository) {
		return args -> {

			var names = Flux.just("Chris", "Brad", "Josh", "Joshua")
				.map(name -> new Customer(UUID.randomUUID().toString(), name))
				.flatMap(repository::save);
			names.subscribe();
		};
	}
}


interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}

record Customer(@Id String id, String name) {
}

@RestController
@RequestMapping("/customers")
record CustomerRestController(CustomerRepository repository) {

	@PostMapping
	Mono<Customer> add(@RequestBody String name) {
		return this.repository.save(new Customer(UUID.randomUUID().toString(), name));
	}

	@GetMapping
	Flux<Customer> get() {
		return this.repository.findAll();
	}
}