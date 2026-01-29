package com.template.hexagonal;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test End-to-End (E2E) - Prueba la aplicación completa
 * Inicia el servidor y hace peticiones HTTP reales
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("UserController - Tests End-to-End")
class UserEndToEndTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    @DisplayName("Flujo completo: Crear usuario exitosamente")
    void shouldCreateUserSuccessfully() {
        String requestBody = """
                {
                    "name": "María García",
                    "email": "maria@example.com",
                    "age": 30
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("María García"))
                .body("email", equalTo("maria@example.com"))
                .body("age", equalTo(30));
    }

    @Test
    @DisplayName("Flujo de error: Intentar crear usuario con email duplicado")
    void shouldFailWhenCreatingDuplicateUser() {
        String requestBody = """
                {
                    "name": "Pedro López",
                    "email": "pedro@example.com",
                    "age": 28
                }
                """;

        // Primera creación - debe funcionar
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(201);

        // Segunda creación - debe fallar
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(409)
                .body("code", equalTo("USER_ALREADY_EXISTS"))
                .body("message", containsString("pedro@example.com"))
                .body("status", equalTo(409));
    }

    @Test
    @DisplayName("Flujo de validación: Rechazar usuario con datos inválidos")
    void shouldRejectInvalidUserData() {
        String requestBody = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "age": -5
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(400)
                .body("code", equalTo("BAD_REQUEST"))
                .body("status", equalTo(400));
    }

    @Test
    @DisplayName("Flujo de validación: Rechazar usuario sin nombre")
    void shouldRejectUserWithoutName() {
        String requestBody = """
                {
                    "email": "test@example.com",
                    "age": 25
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(400)
                .body("code", equalTo("BAD_REQUEST"))
                .body("message", containsString("nombre"));
    }
}
