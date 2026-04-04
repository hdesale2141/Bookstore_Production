package com.example.BookStore_Production.repository;

import com.example.BookStore_Production.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
