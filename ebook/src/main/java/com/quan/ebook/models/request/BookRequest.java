package com.quan.ebook.models.request;

import com.quan.ebook.validator.FormatTypeValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookRequest {
    @NotBlank(message = "the author should not be blank")
    private String author;

    @NotBlank(message = "the title should not be blank")
    private String title;
    
    @FormatTypeValidator
    private String format;
}
