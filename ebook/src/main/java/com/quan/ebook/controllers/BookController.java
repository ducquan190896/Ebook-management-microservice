package com.quan.ebook.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.services.BookService;
import com.quan.ebook.swaggerDocOpenApi.EbookManagementEndpoint;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/ebooks")
public class BookController implements EbookManagementEndpoint {

    @Autowired
    BookService bookService;

    @Override
    public Mono<List<Book>> findAll() {
        return bookService.getAllBooks();
    }

    @Override
    public Mono<BookDto> findById(String ebook_id) {
        return bookService.getBookById(ebook_id);
    }

    @Override
    public Mono<BookDto> save(BookDto book) {
        return bookService.saveBook(book);
    }

    @Override
    public Mono<BookDto> update(String ebook_id, String author, String title, FormatType format) {
        return bookService.updateBook(ebook_id, author, title, format);
    }

    public Mono<Void> deleteById(String ebook_id) {
        return bookService.deleteById(ebook_id);
    }

}
