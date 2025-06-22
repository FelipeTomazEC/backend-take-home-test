package com.noom.interview.fullstack.sleep.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private boolean allowsNull;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        this.allowsNull = constraintAnnotation.allowsNull();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.isBlank()) {
            return allowsNull;
        }

        try {
            LocalDate.parse(date, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
