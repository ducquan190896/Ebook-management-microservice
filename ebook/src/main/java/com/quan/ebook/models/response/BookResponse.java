package com.quan.ebook.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quan.ebook.models.enums.FormatType;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookResponse {
    
    @NotBlank(message = "the author should not be blank")
    @JsonProperty("author")
    private String author;

    @NotBlank(message = "the title should not be blank")
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("format")
    private FormatType format;

}
