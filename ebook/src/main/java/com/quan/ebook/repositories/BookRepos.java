package com.quan.ebook.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.quan.ebook.models.entities.Book;

@Repository
public class BookRepos {

    private List<Book> books = new ArrayList<>();

    public List<Book> getbooks() {
        return this.books;
    }

    public Optional<Book> getBookById(String id) {
        Optional<Book> bookOptional = this.books.stream().filter(b -> b.getId().equals(id)).findFirst();
        return bookOptional;
    }

    public Book saveBook(Book book) {
        Optional<Book> bookOptional = getBookByTitle(book.getTitle());
        if (!bookOptional.isPresent()) {
            book.setId(UUID.randomUUID().toString());
            this.books.add(book);
            return book;
        } else {
            Book bookSaved = bookOptional.get();
            this.books = this.books.stream().map(b -> b.getId().equals(bookSaved.getId()) ? bookSaved : b)
                    .collect(Collectors.toList());
            return bookSaved;
        }
    }

    public void deleteBook(Book book) {
        this.books.remove(book);
    }

    public void deleteAll() {
        this.books = new ArrayList<>();
    }

    public Optional<Book> getBookByTitle(String title) {
        Optional<Book> bookOptional = this.books.stream().filter(b -> b.getTitle().equals(title)).findFirst();
        return bookOptional;
    }

    // check duplication of author and title
    public boolean checkDuplicatedTitle(String title) {
        Optional<Book> bookOptional = this.books.stream().filter(b -> b.getTitle().equals(title)).findFirst();
        if (bookOptional.isPresent()) {
            return true;
        }
        return false;
    }

}
