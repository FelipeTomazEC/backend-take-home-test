package com.noom.interview.fullstack.sleep.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext ctx) {
        if (dateString == null || dateString.isBlank()) {
            return false;
        }

        try {
            LocalDateTime.parse(dateString, FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
