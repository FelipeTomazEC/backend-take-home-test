package com.noom.interview.fullstack.sleep.infrastructure.validation

import spock.lang.Specification
import spock.lang.Subject

import javax.validation.ConstraintValidatorContext

class DateValidatorTest extends Specification {

    @ValidDate(allowsNull = false)
    String dateFieldNotNull

    @ValidDate(allowsNull = true)
    String dateFieldAllowNull

    @Subject
    DateValidator validator = new DateValidator()

    def "Validate dates and does not accept null or blank strings"() {
        given:
        validator.initialize(DateValidatorTest.getDeclaredField("dateFieldNotNull").getAnnotation(ValidDate))
        ConstraintValidatorContext context = Mock()

        expect:
        validator.isValid(value, context) == expected

        where:
        value                   | expected
        "2024-06-01"    | true
        null                      | false
        ""                         | false
        " "                        | false
        "invalid"             | false
    }

    def "Accept null and blank strings"() {
        given:
        validator.initialize(DateValidatorTest.getDeclaredField("dateFieldAllowNull")
                .getAnnotation(ValidDate))
        ConstraintValidatorContext context = Mock()

        expect:
        validator.isValid(value, context) == expected

        where:
        value                   | expected
        "2024-06-01"    | true
        null                      | true
        ""                         | true
        " "                        | true
        "invalid"              | false
    }
}