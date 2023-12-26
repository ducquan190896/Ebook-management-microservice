package com.quan.ebook.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = BookDtoValidatorConstraint.class)
public @interface FormatTypeValidator {
    String message() default "format must be in precise format (pdf, mobi, epub, azw, txt)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; 
}
