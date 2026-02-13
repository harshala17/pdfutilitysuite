package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidDocs;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class WordFilesValidator implements ConstraintValidator<ValidDocs, MultipartFile[]> {

    @Override
    public void initialize(ValidDocs constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(files).allMatch(BaseValidatorUtility::isValidDoc);
    }
}
