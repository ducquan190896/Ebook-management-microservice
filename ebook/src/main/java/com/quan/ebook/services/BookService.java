package com.quan.ebook.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.models.request.BookRequest;
import com.quan.ebook.models.response.BookResponse;
import com.quan.ebook.models.response.BookListResponse;
import com.quan.ebook.repositories.BookRepos;
import reactor.core.publisher.Mono;

@Service
public class BookService {
    @Autowired
    BookRepos bookRepos;
    @Autowired
    BookMapper bookMapper;

    public Mono<BookListResponse> getAllBooks() {
        return Mono.fromCallable(() -> bookRepos.getbooks())
                .map(books -> BookListResponse.builder().data(books).build());
    }

    public Mono<BookResponse> getBookById(String id) {
        return Mono.fromCallable(() -> bookRepos.getBookById(id))
                .flatMap(bookOrNot -> bookOrNot
                        .map(book -> Mono.just(bookMapper.mapBookToBookResponse(book)))
                        .orElseGet(Mono::empty))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found with id: " + id)));
    }

    public Mono<BookResponse> saveBook(BookRequest req) {
        Book book = Book.builder()
                .author(req.getAuthor())
                .title(req.getTitle())
                .format(FormatType.valueOf(req.getFormat()))
                .build();
                
        return Mono.fromCallable(() -> bookRepos.checkDuplicatedTitle(req.getTitle(), req.getAuthor()))
                .filter(isDuplicatedOrNot -> isDuplicatedOrNot == false)
                .map(__ -> {
                    Book bookSaved = bookRepos.saveBook(book);
                    return bookMapper.mapBookToBookResponse(bookSaved);
                })
                .switchIfEmpty(Mono.error(new BadResultException("the book title and author is duplicated")));
    }

    public Mono<BookResponse> updateBook(String id, String author, String title, String format) {
        return Mono.fromCallable(() -> bookRepos.getBookById(id))
                .filter(Optional::isPresent)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found with id: " + id)))
                .flatMap(bookOrNot -> bookOrNot
                        .filter(__ -> bookRepos.checkDuplicatedTitle(title, author) == false)
                        .map(book -> {
                            Book newBook = updateNewBook(book, author, title, FormatType.valueOf(format));
                            return Mono.just(bookMapper.mapBookToBookResponse(newBook));
                        })
                        .orElseGet(() -> Mono.empty()))
                .switchIfEmpty(Mono.error(new BadResultException("the book title and author is duplicated")));
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
        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (format != null) {
            book.setFormat(format);
        }
        return bookRepos.saveBook(book);
    }

}
