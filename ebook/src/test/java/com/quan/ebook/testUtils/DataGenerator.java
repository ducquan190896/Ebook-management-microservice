package com.quan.ebook.testUtils;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.models.request.BookRequest;
import com.quan.ebook.models.response.BookResponse;

@Component
public class DataGenerator {

     public Book generateBook() {
        return Book.builder()
                            .id(generateUUID())
                            .author("abc")
                            .title("abc")
                            .format(FormatType.epub)
                            .build();
    }

    public List<Book> generateBookList() {
        Book book1 = Book.builder()
                .id(generateUUID())
                .author("abc")
                .title("abc_title")
                .format(FormatType.epub)
                .build();
        Book book2 = Book.builder()
                .id(generateUUID())
                .author("abc2")
                .title("abc2_title")
                .format(FormatType.epub)
                .build();
        return List.of(book1, book2);
    }

    public BookResponse convertBookToBookResponse(Book book) {
        return BookResponse.builder()
                            .author(book.getAuthor())
                            .title(book.getTitle())
                            .format(book.getFormat())
                            .build();
    }

    public BookRequest convertBookToBookRequest(Book book) {
        return BookRequest.builder()
                            .author(book.getAuthor())
                            .title(book.getTitle())
                            .format(book.getFormat().getName())
                            .build();
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public Book getFirstBookFromSampleList() {
        return generateBookList().get(0);
    }
}
