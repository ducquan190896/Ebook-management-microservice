package com.quan.ebook.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.validator.EnumValidator;

import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
    
    @NotBlank(message = "the author should not be blank")
    @JsonProperty("author")
    private String author;

    @NotBlank(message = "the title should not be blank")
    @JsonProperty("title")
    private String title;

     // @EnumValidator(enumClass = FormatType.class)
    @JsonProperty("format")
    private FormatType format;
    
}
