package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidDoc;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class WordFileValidator implements ConstraintValidator<ValidDoc, MultipartFile> {

    @Override
    public void initialize(ValidDoc constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return BaseValidatorUtility.isValidDoc(file);
    }
}
