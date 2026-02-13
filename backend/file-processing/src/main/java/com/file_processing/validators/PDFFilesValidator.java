package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidPDFs;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class PDFFilesValidator implements ConstraintValidator<ValidPDFs, MultipartFile[]> {

    @Override
    public void initialize(ValidPDFs constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(multipartFiles)
            .allMatch(BaseValidatorUtility::isValidPDF);
    }
}
