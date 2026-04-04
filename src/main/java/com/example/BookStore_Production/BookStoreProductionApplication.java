package com.example.BookStore_Production;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookStoreProductionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreProductionApplication.class, args);
	}

}
