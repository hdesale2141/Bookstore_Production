package com.example.BookStore_Production.service;

import com.example.BookStore_Production.dto.BookEntryRequestDTO;
import com.example.BookStore_Production.dto.BookEntryResponseDTO;
import com.example.BookStore_Production.dto.BookRequestDTO;
import com.example.BookStore_Production.dto.BookResponseDTO;
import com.example.BookStore_Production.entity.Book;
import com.example.BookStore_Production.entity.BookEntry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO request);
    void deleteBook(Long id);
    BookResponseDTO getBook(Long id);
    Page<BookResponseDTO> getAllBooks(int page, int size);
    Page<BookEntryResponseDTO> getAllEntries(int page, int size);
    BookEntryResponseDTO addEntry(Long bookId, BookEntryRequestDTO request);
    List<BookEntry> getBookEntryById(Long id);
    List<Book> getAlltheBoooks();
}
