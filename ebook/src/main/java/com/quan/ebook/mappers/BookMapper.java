package com.quan.ebook.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.response.BookResponse;


@Component
public class BookMapper {
    @Autowired
    ModelMapper modelMapper;

    public BookResponse mapBookToBookResponse(Book book) {
        return modelMapper.map(book, BookResponse.class);
    }
}
