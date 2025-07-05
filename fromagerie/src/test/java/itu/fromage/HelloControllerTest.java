package itu.fromage;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testHello() {
        when().get("/")
                .then()
                .statusCode(200)
                .body(is("Hello World!"));
    }

    @Test
    public void testCalc() {
        given()
                .param("left", 100)
                .param("right", 200)
                .when()
                .get("/calc")
                .then()
                .statusCode(200)
                .body("left", is(100))
                .body("right", is(200))
                .body("answer", is(300));
    }
}
