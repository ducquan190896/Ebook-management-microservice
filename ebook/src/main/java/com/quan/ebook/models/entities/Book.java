package com.quan.ebook.models.entities;

import com.quan.ebook.models.enums.FormatType;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Book {
    private String id;
    private String author;
    private String title;
    private FormatType format;

    public Book(String author, String title, FormatType format) {
        this.author = author;
        this.title = title;
        this.format = format;
    }
}

