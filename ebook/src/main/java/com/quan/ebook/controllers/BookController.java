package com.quan.ebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.dto.BookListDto;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.services.BookService;
import com.quan.ebook.swaggerDocOpenApi.EbookManagementEndpoint;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/ebooks")
public class BookController implements EbookManagementEndpoint {

    @Autowired
    BookService bookService;

    @Override
    public Mono<ResponseEntity<BookListDto>> findAll() {
        return bookService.getAllBooks().map(books -> new ResponseEntity<>(books, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<BookDto>> findById(String ebook_id) {
        return bookService.getBookById(ebook_id).map(book -> new ResponseEntity<>(book, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<BookDto>> save(BookDto book) {
        return bookService.saveBook(book).map(newBook -> new ResponseEntity<>(newBook, HttpStatus.CREATED));
    }

    @Override
    public Mono<ResponseEntity<BookDto>> update(String ebook_id, String author, String title, FormatType format) {
        return bookService.updateBook(ebook_id, author, title, format).map(book -> new ResponseEntity<>(book, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<HttpStatus>> deleteById(String ebook_id) {
        return bookService.deleteById(ebook_id).map(__ -> new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT));
    }

}
