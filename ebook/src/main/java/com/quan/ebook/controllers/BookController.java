package com.quan.ebook.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.quan.ebook.swaggerDocOpenApi.BookControllerOpenApi;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ebooks")
public class BookController implements BookControllerOpenApi{
    
    @Autowired
    BookService bookService;

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Mono<List<Book>> findAll() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/{ebook_id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Mono<BookDto> findById(@PathVariable(value = "ebook_id") String ebook_id) {
        return bookService.getBookById(ebook_id);
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public Mono<BookDto> save(@RequestBody @Valid BookDto book) {
        return bookService.saveBook(book);
    }

    @PutMapping(value = "/{ebook_id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Mono<BookDto> update(@PathVariable(value = "ebook_id") String ebook_id, 
                                @RequestParam(required = false, value = "author") String author,  @RequestParam(required = false, value = "title") String title,  @RequestParam(required = false, value = "format") FormatType format
                                ) {
        return bookService.updateBook(ebook_id, author, title, format);
    }

    @DeleteMapping(value = "/{ebook_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable(value = "ebook_id") String ebook_id) {
        return bookService.deleteById(ebook_id);
    }
    
}

