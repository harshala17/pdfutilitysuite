package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidPPT;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PPTFileValidator implements ConstraintValidator<ValidPPT, MultipartFile> {

    @Override
    public void initialize(ValidPPT constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Use @NotNull to handle empty files separately
        }

        var contentType = file.getContentType();
        var fileName = file.getOriginalFilename();

        // Check MIME types for PowerPoint
        var isValidMimeType = "application/vnd.ms-powerpoint".equals(contentType) ||
            "application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(contentType);

        // Check file extension as a fallback
        var isValidExtension = fileName != null && (fileName.toLowerCase().endsWith(".ppt") || fileName.toLowerCase().endsWith(".pptx"));

        return isValidMimeType || isValidExtension;
    }
}
