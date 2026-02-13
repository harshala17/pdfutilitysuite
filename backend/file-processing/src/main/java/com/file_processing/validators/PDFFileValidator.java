package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidPDF;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PDFFileValidator implements ConstraintValidator<ValidPDF, MultipartFile> {

    @Override
    public void initialize(ValidPDF constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return BaseValidatorUtility.isValidPDF(file);
    }
}
