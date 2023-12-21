package com.quan.ebook.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.quan.ebook.models.entities.Book;


@Repository
public class BookRepos {
    
    private List<Book> books = new ArrayList<>(); 
    

    public List<Book> getbooks() {
        return books;
    }

    public Optional<Book> getBookById(String id) {
       Optional<Book> bookOptional = books.stream().filter(b -> b.getId().equals(id)).findFirst();
       return bookOptional;
    }

    public Book saveBook(Book book) {
        Optional<Book> bookOptional = getBookById(book.getId());
        if(!bookOptional.isPresent()) {
            books.add(book);
        } else {
            books = books.stream().map(b -> b.getId().equals(book.getId()) ? book : b).collect(Collectors.toList());
        }
        return book;
    }

    public void deleteBook(Book book) {
        books.remove(book);   
    }
}
