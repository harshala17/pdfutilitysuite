package com.file_processing.validators.annotations;

import com.file_processing.validators.CSVFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Can be used on fields or method params
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CSVFileValidator.class)
public @interface ValidCSV {
    String message() default "Invalid file format. Only csv file is allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
