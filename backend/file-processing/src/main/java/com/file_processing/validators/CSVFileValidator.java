package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidCSV;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class CSVFileValidator implements ConstraintValidator<ValidCSV, MultipartFile> {

    @Override
    public void initialize(ValidCSV constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return BaseValidatorUtility.isValidCSV(file);
    }
}
