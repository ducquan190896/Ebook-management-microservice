package com.quan.ebook.validator;

import static org.mockito.Answers.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.quan.ebook.models.enums.FormatType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.ws.rs.BadRequestException;

public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, Enum> {
    private List<String> acceptedValues;

    // @Override
    // public void initialize(EnumValidator constraintAnnotation) {
    //     acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
    //     .map(Enum::name)
    //     .collect(Collectors.toList());
    // }

    // @Override
    // public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    //     if (value == null) {
    //         return true;
    //     }

    //     return acceptedValues.contains(value.toString());
    // }

    // List<Enum> valueList = null;

    // @Override
    // public boolean isValid(Enum value, ConstraintValidatorContext context) {

    //     return valueList.contains(value);
    // }

    // @Override
    // public void initialize(EnumValidator constraintAnnotation) {

    //     Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

    //     List<String> enumValueList = Arrays.asList(constraintAnnotation.values());

    //     Enum<?>[] enumValues = enumClass.getEnumConstants();
    //     valueList = Arrays.stream(enumValues)
    //                       .filter(e -> enumValueList.contains(e.name()))
    //                       .collect(Collectors.toList());

    // }

    private List<String> enumValues = new ArrayList<>();

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        // enumValues = constraintAnnotation.values();
        enumValues.add(FormatType.azw.getName());
        enumValues.add(FormatType.pdf.getName());
        enumValues.add(FormatType.epub.getName());
        enumValues.add(FormatType.mobi.getName());
        enumValues.add(FormatType.txt.getName());
    }

   @Override
   public boolean isValid(Enum value, ConstraintValidatorContext context) {
    for (String enumValue : enumValues) {
        if (enumValue.equals(value.name())) {
            return true;
        }
    }

    // throw new BadRequestException("the {value} not in precise format");
    return false;
   }
}