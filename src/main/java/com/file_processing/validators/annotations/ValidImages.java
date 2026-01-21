package com.file_processing.validators.annotations;

import com.file_processing.validators.ImageFilesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Can be used on fields or method params
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFilesValidator.class)
public @interface ValidImages {
    String message() default "Invalid file format. Only Images are allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
