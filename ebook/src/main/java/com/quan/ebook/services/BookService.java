package com.quan.ebook.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.dto.BookListDto;
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

    public Mono<BookListDto> getAllBooks() {
        return Mono.fromCallable(() -> bookRepos.getbooks())
                .map(books -> BookListDto.builder().data(books).build());
    }

    public Mono<BookDto> getBookById(String id) {
        return Mono.fromCallable(() -> bookRepos.getBookById(id))
                .flatMap(bookOrNot -> bookOrNot
                        .map(book -> Mono.just(bookMapper.mapBookToBookDto(book)))
                        .orElseGet(Mono::empty))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found with id: " + id)));
    }

    public Mono<BookDto> saveBook(BookDto req) {
        Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .author(req.getAuthor())
                .title(req.getTitle())
                .format(req.getFormat())
                .build();

        return Mono.fromCallable(() -> bookRepos.saveBook(book))
                .map(bookMapper::mapBookToBookDto);
    }

    public Mono<BookDto> updateBook(String id, String author, String title, FormatType format) {
        return Mono.fromCallable(() -> bookRepos.getBookById(id))
                .flatMap(bookOrNot -> bookOrNot
                        .map(book -> {
                            Book newBook = updateNewBook(book, author, title, format);
                            return Mono.just(bookMapper.mapBookToBookDto(newBook));
                        })
                        .orElseGet(() -> Mono.empty()))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found with id: " + id)));
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromCallable(() -> bookRepos.getBookById(id))
                .filter(Optional::isPresent)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found with id: " + id)))
                .map(Optional::get)
                .flatMap(book -> {
                    bookRepos.deleteBook(book);
                    return Mono.empty();
                });
    }

    public Book updateNewBook(Book book, String author, String title, FormatType format) {
        if (author != null) {
            book.setAuthor(author);
        }
        if (title != null) {
            book.setTitle(title);
        }
        if (format != null) {
            book.setFormat(format);
        }
        return bookRepos.saveBook(book);
    }

}
