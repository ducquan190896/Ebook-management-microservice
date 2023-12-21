package com.quan.ebook.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.repositories.BookRepos;
import reactor.core.publisher.Mono;

@Service
public class BookService {
    @Autowired
    BookRepos bookRepos;
    @Autowired
    BookMapper bookMapper;
    
    public Mono<List<Book>> getAllBooks() {
        List<Book> bookList = bookRepos.getbooks();
        Mono<List<Book>> books = Mono.just(bookList);
        return books;
    }

    public Mono<BookDto> getBookById(String id) {
        Book book = bookRepos.getBookById(id).orElseThrow(() -> new EntityNotFoundException("the book not found"));
        BookDto bookDto = bookMapper.mapBookToBookDto(book);
        return Mono.just(bookDto);
    }

    public Mono<BookDto> saveBook(BookDto req) {
        Book book = Book.builder()
                            .id(UUID.randomUUID().toString())
                            .author(req.getAuthor())
                            .title(req.getTitle())
                            .format(req.getFormat())
                            .build();
        bookRepos.saveBook(book);
        BookDto bookDto = bookMapper.mapBookToBookDto(book);
        return Mono.just(bookDto);
    }

    public Mono<BookDto> updateBook(String id, String author, String title, FormatType format) {
        Book book = bookRepos.getBookById(id).orElseThrow(() -> new EntityNotFoundException("the book not found"));
        if(!author.isEmpty()) {
            book.setAuthor(author);
        }
        if(!title.isEmpty()) {
            book.setTitle(title);
        }
        if(format != null) {
            book.setFormat(format);
        }
        bookRepos.saveBook(book);
        BookDto bookDto = bookMapper.mapBookToBookDto(book);
        return Mono.just(bookDto);
    }

    public void deleteById(String id) {
        Book book = bookRepos.getBookById(id).orElseThrow(() -> new EntityNotFoundException("the book not found"));
        bookRepos.deleteBook(book);
    }
}

