package com.noom.interview.fullstack.sleep.infrastructure.validation

import spock.lang.Specification
import spock.lang.Subject

class DateTimeValidatorTest extends Specification {

    @Subject
    def validator = new DateTimeValidator()

    def "Validates that a date-time string is in ISO-8601 format"() {
        expect:
        validator.isValid("2023-10-01T12:00:00", null)
        validator.isValid("2023-10-01T12:00", null)
        !validator.isValid("2023-10-01T12", null)
        !validator.isValid("2023-13-01T12:00:00", null)
        !validator.isValid("2023-06-32T12:00:00", null)
        !validator.isValid("2023-06-05T26:00:00", null)
        !validator.isValid("2023-06-05T12:70:00", null)
        !validator.isValid("2023-06-05T12:30:70", null)
        !validator.isValid("        ", null)
        !validator.isValid(null, null)
    }
}
