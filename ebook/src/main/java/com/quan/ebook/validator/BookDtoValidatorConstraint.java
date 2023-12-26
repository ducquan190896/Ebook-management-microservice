package com.quan.ebook.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.quan.ebook.models.enums.FormatType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookDtoValidatorConstraint implements ConstraintValidator<FormatTypeValidator, String> {

    @Override
    public boolean isValid(String format, ConstraintValidatorContext context) {
        List<String> enumValues = Arrays.stream(FormatType.values())
                .map(FormatType::getName)
                .collect(Collectors.toList());

        if (!enumValues.contains(format)) {
            return false;
        }
        return true;
    }
}