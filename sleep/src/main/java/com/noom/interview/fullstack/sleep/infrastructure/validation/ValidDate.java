package com.noom.interview.fullstack.sleep.infrastructure.validation;


import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface ValidDate {
    boolean allowsNull() default false;
    String message() default "Expected format: yyyy-MM-dd";
    Class<?>[] groups() default {};
    Class<? extends javax.validation.Payload>[] payload() default {};
}
