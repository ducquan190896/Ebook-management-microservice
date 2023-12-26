package com.quan.ebook.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.validator.BookDtoValidator;

import jakarta.validation.constraints.NotBlank;

// seperate bookDto into EBookRequest and EbookResponse
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@BookDtoValidator
public class BookDto {
    
    @NotBlank(message = "the author should not be blank")
    @JsonProperty("author")
    private String author;

    @NotBlank(message = "the title should not be blank")
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("format")
    private FormatType format;
    // convert format into string from eBookRequest
    
}
