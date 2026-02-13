package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidCSVOrExcel;
import com.file_processing.validators.utils.BaseValidatorUtility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class CSVOrExcelFileValidator implements ConstraintValidator<ValidCSVOrExcel, MultipartFile> {

    @Override
    public void initialize(ValidCSVOrExcel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return BaseValidatorUtility.isValidCsvOrExcel(file);
    }
}
