package com.noom.interview.fullstack.sleep.testutils

import com.noom.interview.fullstack.sleep.SleepApplication
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType

import static io.restassured.config.EncoderConfig.encoderConfig
import static io.restassured.config.RestAssuredConfig.config

@SpringBootTest(
        classes = SleepApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AbstractControllerTest extends AbstractDatabaseTest {
    @LocalServerPort
    int port

    def createHttpRequest() {
        RestAssured.defaultParser = Parser.JSON

        return RestAssured.given()
                .port(port)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .config(config().encoderConfig(
                        encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                )
    }
}
