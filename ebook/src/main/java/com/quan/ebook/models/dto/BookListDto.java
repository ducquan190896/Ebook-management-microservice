package com.quan.ebook.models.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quan.ebook.models.entities.Book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookListDto {
    @JsonProperty("data")
    private List<Book> data;
}
