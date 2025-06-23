package com.noom.interview.fullstack.sleep.infrastructure.validation

import spock.lang.Specification
import spock.lang.Subject

import javax.validation.ConstraintValidatorContext

class DateTimeValidatorTest extends Specification {

    @ValidDateTime(allowsNull = false)
    String dateTimeFieldNotNull

    @ValidDateTime(allowsNull = true)
    String dateTimeFieldAllowNull

    @Subject
    DateTimeValidator validator = new DateTimeValidator()

    def "Validate date-times and does not accept null"() {
        given:
        validator.initialize(DateTimeValidatorTest.getDeclaredField("dateTimeFieldNotNull").getAnnotation(ValidDateTime))
        ConstraintValidatorContext context = Mock()

        expect:
        validator.isValid(value, context) == expected

        where:
        value                                        | expected
        "2023-10-01T12:00:00"        | true
        "2023-10-01T12:00"             | true
        "2023-10-01T12"                   | false
        "2023-13-01T12:00:00"        | false
        "2023-06-32T12:00:00"        | false
        "2023-06-05T26:00:00"        | false
        "2023-06-05T12:70:00"        | false
        "2023-06-05T12:30:70"        | false
        "        "                                      | false
        null                                           | false
        ""                                              | false
        "invalid"                                  | false
    }

    def "Accept null values"() {
        given:
        validator.initialize(DateTimeValidatorTest.getDeclaredField("dateTimeFieldAllowNull").getAnnotation(ValidDateTime))
        ConstraintValidatorContext context = Mock()

        expect:
        validator.isValid(value, context) == expected

        where:
        value                                        | expected
        "2023-10-01T12:00:00"        | true
        null                                           | true
        ""                                              | false
        "        "                                      | false
        "invalid"                                  | false
    }
}
