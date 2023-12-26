package com.quan.ebook.validator;

import static org.mockito.Answers.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.enums.FormatType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookDtoValidatorConstraint implements ConstraintValidator<BookDtoValidator, BookDto> {

    @Override
    public boolean isValid(BookDto bookDto, ConstraintValidatorContext context) {
        List<String> enumValues = Arrays.stream(FormatType.values())
                .map(FormatType::getName)
                .collect(Collectors.toList());

        if (!enumValues.contains(bookDto.getFormat().getName())) {
            return false;
        }
        return true;
    }
}