package com.example.BookStore_Production.service;

import com.example.BookStore_Production.dto.BookEntryRequestDTO;
import com.example.BookStore_Production.dto.BookEntryResponseDTO;
import com.example.BookStore_Production.dto.BookRequestDTO;
import com.example.BookStore_Production.dto.BookResponseDTO;
import com.example.BookStore_Production.entity.Book;
import com.example.BookStore_Production.entity.BookEntry;
import com.example.BookStore_Production.exception.ResourceNotFoundException;
import com.example.BookStore_Production.repository.BookEntryRepository;
import com.example.BookStore_Production.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final BookEntryRepository bookEntryRepository;

    public BookServiceImpl(BookRepository bookRepository, BookEntryRepository bookEntryRepository) {
        this.bookRepository = bookRepository;
        this.bookEntryRepository = bookEntryRepository;
    }
    @Override
    @CacheEvict(value = "bookList", allEntries = true)
    public BookResponseDTO createBook(BookRequestDTO request){
        logger.info("Creating new book with title {}",request.getTitle());
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setImage(request.getImage());
        book.setPrice(request.getPrice());

        Book saved = bookRepository.save(book);
        logger.info("Book Created successfully with id: {}", saved.getId());
        return mapToBookResponse(saved);
    }
    @Override
    @Cacheable(value = "books",key="#id")
    public BookResponseDTO getBook(Long id){
        logger.info("🔥 DB HIT for book id : {}", id);
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Book not found"));
        return mapToBookResponse(book);
    }
    @Override
    @Cacheable(value = "bookList", key="#page + '-' + #size")
    public Page<BookResponseDTO> getAllBooks(int page, int size){
        logger.info("Fetching all books from database.");
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(this::mapToBookResponse);
    }

    @Override
    public List<Book> getAlltheBoooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }
    @Override
    @CacheEvict(value = {"books","bookList"}, allEntries = true)
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookEntryResponseDTO> getAllEntries(int page, int size){
        logger.info("Fetching book entries from database.");
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntry> entries = bookEntryRepository.findAll(pageable);
        return entries.map(this::mapToEntryResponse);
    }
    @Override
    public BookEntryResponseDTO addEntry(Long bookId, BookEntryRequestDTO request){
        logger.info("Adding entry to book with Id: {}",bookId);
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new ResourceNotFoundException("Book not found"));

        BookEntry entry = new BookEntry();
        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setDate(LocalDateTime.now());

        entry.setBook(book);

        BookEntry saved = bookEntryRepository.save(entry);

        logger.info("Book entry Created successfully with Id: {}",saved.getId());

        return mapToEntryResponse(saved);
    }

    @Override
    public List<BookEntry> getBookEntryById(Long id){
       List<BookEntry> bookEntry = bookEntryRepository.findByBookId(id);
        return bookEntry;
    }
    private BookResponseDTO mapToBookResponse(Book book){
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setImage(book.getImage());
        dto.setPrice(book.getPrice());

        return dto;
    }
    private BookEntryResponseDTO mapToEntryResponse(BookEntry entry){
        BookEntryResponseDTO dto = new BookEntryResponseDTO();
        dto.setId(entry.getId());
        dto.setTitle(entry.getTitle());
        dto.setContent(entry.getContent());
        dto.setDate(entry.getDate());

        return dto;
    }
}
