package com.noom.interview.fullstack.sleep.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        var validValues = Arrays.stream(this.enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());

        var isValid = validValues.contains(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("Valid values are:  %s", String.join(", ", validValues)))
                .addConstraintViolation();
        }

        return isValid;
    }
}
