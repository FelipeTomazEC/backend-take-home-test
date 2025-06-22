package com.noom.interview.fullstack.sleep.infrastructure.validation

import spock.lang.Specification
import spock.lang.Subject

import javax.validation.ConstraintValidatorContext

class EnumValidatorTest extends Specification {

    private enum TestEnum {
        THIS_IS_VALID,
        THIS_IS_ALSO_VALID
    }

    @ValidEnum(enumClass = TestEnum.class)
    String enumField

    @Subject
    EnumValidator validator = new EnumValidator()

    def setup() {
        validator.initialize(EnumValidatorTest.getDeclaredField("enumField")
                .getAnnotation(ValidEnum))
    }

    def "Validates that a string is a valid enum value"() {
        given:
        ConstraintValidatorContext context = Mock()

        when:
        def validationResult = validator.isValid(value, context)

        then: "the validation result matches the expected outcome"
        validationResult == expected

        and: "add valid values to error message when validation fails"
        context.buildConstraintViolationWithTemplate({ String messageTemplate ->
            def validNames = TestEnum.values().collect { it.name() }
            assert validNames.every { messageTemplate.contains(it) }
        } as String) >> Mock(ConstraintValidatorContext.ConstraintViolationBuilder)

        where:
        value                                    | expected
        "THIS_IS_VALID"               | true
        "THIS_IS_ALSO_VALID"   | true
        "INVALID_VALUE"            | false
        null                                       | false
        ""                                          | false
        " "                                         | false
    }
}
