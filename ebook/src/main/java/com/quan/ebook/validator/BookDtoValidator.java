package com.quan.ebook.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Constraint(validatedBy = BookDtoValidatorConstraint.class)
public @interface BookDtoValidator {
    // Class<? extends Enum<?>> enumClass();
    String message() default "Value must be in precise format {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; 
}
