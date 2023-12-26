package com.quan.ebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quan.ebook.models.request.BookRequest;
import com.quan.ebook.models.response.BookResponse;
import com.quan.ebook.models.response.BookListResponse;
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
    public Mono<ResponseEntity<BookListResponse>> findAll() {
        return bookService.getAllBooks().map(books -> new ResponseEntity<>(books, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<BookResponse>> findById(String ebook_id) {
        return bookService.getBookById(ebook_id).map(book -> new ResponseEntity<>(book, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<BookResponse>> save(BookRequest bookRequest) {
        return bookService.saveBook(bookRequest).map(newBook -> new ResponseEntity<>(newBook, HttpStatus.CREATED));
    }

    @Override
    public Mono<ResponseEntity<BookResponse>> update(String ebook_id, String author, String title, String format) {
        return bookService.updateBook(ebook_id, author, title, format)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<HttpStatus>> deleteById(String ebook_id) {
        return bookService.deleteById(ebook_id).then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

}
