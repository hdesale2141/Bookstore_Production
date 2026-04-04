package com.example.BookStore_Production.controller;

import com.example.BookStore_Production.dto.*;

import com.example.BookStore_Production.entity.Book;
import com.example.BookStore_Production.entity.BookEntry;
import com.example.BookStore_Production.service.BookService;


import com.example.BookStore_Production.service.UserService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
//@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO request){
        BookResponseDTO response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        BookResponseDTO response = bookService.getBook(id);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getAllBooks(
            @RequestParam int page,
            @RequestParam int size){
        Page<BookResponseDTO> books = bookService.getAllBooks(page,size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAlltheBooks(){
        List<Book> books = bookService.getAlltheBoooks();
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        BookResponseDTO book = bookService.getBook(id);

        if(book == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book Deleted Successfully....");
    }
    @GetMapping("/entries")
    public ResponseEntity<Page<BookEntryResponseDTO>> getAllEntries(
            @RequestParam int page,
            @RequestParam int size
    ){
        Page<BookEntryResponseDTO> entries = bookService.getAllEntries(page,size);
        return ResponseEntity.ok(entries);
    }
    @GetMapping("/entries/{id}")
    public List<BookEntry> findBookEntryById(@PathVariable Long id){
        return bookService.getBookEntryById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/entry/{bookId}")
    public ResponseEntity<BookEntryResponseDTO> createBookEntry(@PathVariable Long bookId, @Valid @RequestBody BookEntryRequestDTO request){
        BookEntryResponseDTO response = bookService.addEntry(bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
