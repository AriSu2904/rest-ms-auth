package com.service.authorization.utils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationUtils {

    private final Validator validator;

    public <T> void validate(T obj) {
        Set<ConstraintViolation<Object>> validate = validator.validate(obj);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }

    public static Map<String, Object> mapConstraintViolationException(Set<ConstraintViolation<?>> constraintViolations) {
        Map<String, Object> result = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            result.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        return result;
    }

}