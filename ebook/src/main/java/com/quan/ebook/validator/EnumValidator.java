package com.quan.ebook.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Constraint(validatedBy = EnumValidatorConstraint.class)
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();
    String[] values();
    String message() default "Value must be in precise format {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; 
}
