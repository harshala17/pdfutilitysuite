package com.file_processing.validators.annotations;


import com.file_processing.validators.WordFilesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Can be used on fields or method params
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WordFilesValidator.class)
public @interface ValidDocs {
    String message() default "Invalid file format. Only docx are allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
