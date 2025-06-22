package com.noom.interview.fullstack.sleep.testutils

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification


@SpringBootTest
abstract class AbstractDatabaseTest extends Specification {
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("sleep_test_db")
            .withUsername("sleep_test_user")
            .withPassword("sleep_test_password")

    def setupSpec() {
        POSTGRES.start()
    }

    def cleanupSpec() {
        POSTGRES.stop()
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl)
        registry.add("spring.datasource.username", POSTGRES::getUsername)
        registry.add("spring.datasource.password", POSTGRES::getPassword)
    }
}
