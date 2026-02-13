package com.file_processing.validators;

import com.file_processing.validators.annotations.ValidImages;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageFilesValidator implements ConstraintValidator<ValidImages, MultipartFile[]> {

    // Define supported image types
    private static final List<String> SUPPORTED_TYPES = Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/bmp",
        "image/webp"
    );


    @Override
    public void initialize(ValidImages constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(multipartFiles)
            .allMatch(file ->
                ObjectUtils.isNotEmpty(file)
                    && isSupportedContentType(file.getContentType())
            );
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType != null && SUPPORTED_TYPES.contains(contentType.toLowerCase());
    }
}
