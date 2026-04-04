package com.example.BookStore_Production.repository;

import com.example.BookStore_Production.entity.BookEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookEntryRepository extends JpaRepository<BookEntry, Long> {
    List<BookEntry> findByBookId(Long id);
}
