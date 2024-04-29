package cz.ff.jsframework;

import cz.ff.jsframework.api.model.JsFrameworkViewDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * This test should be considered as an E2E integration test independent on the application itself.
 * It's a black box testing, so it's not affected by changes in application's implementation if API is not changed.
 * In practice, it can be replaced by some real testing framework like Cucumber for example.
 * Here we test happy scenarios mainly.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsFrameworkApplicationE2ETest {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
//        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    private final JsFrameworkViewDTO jsFrameworkReact = new JsFrameworkViewDTO()
            .name("React")
            .version("10.0.1")
            .deprecationDate(LocalDate.parse("2026-10-12"))
            .hypeLevel(9)
            ;

    private final JsFrameworkViewDTO jsFrameworkJQuery = new JsFrameworkViewDTO()
            .name("jQuery")
            .version("1.2.3")
            .deprecationDate(LocalDate.parse("2014-10-12"))
            .hypeLevel(5)
            ;

    private final JsFrameworkViewDTO jsFrameworkWithValuesForUpdate = new JsFrameworkViewDTO()
            .deprecationDate(LocalDate.parse("2000-01-01"))
            .hypeLevel(0)
            ;

    @Test
    @Order(1)
    void testGetAllAndResultIsEmpty(){
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    @Test
    @Order(2)
    void testCreateNew(){
        JsFrameworkViewDTO framework = jsFrameworkReact;
        given().contentType(ContentType.JSON)
                .body(framework)
                .when()
                .put("/js-frameworks/{name}/{version}", framework.getName(), framework.getVersion())
                .then()
                .statusCode(201)
                ;
    }

    @Test
    @Order(3)
    void testGetAllAndOnePreviouslyCreatedIsReturned(){
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(1));
    }

    @Test
    @Order(4)
    void testCreateAnotherOne(){
        JsFrameworkViewDTO framework = jsFrameworkJQuery;
        given().contentType(ContentType.JSON)
                .body(framework)
                .when()
                .put("/js-frameworks/{name}/{version}",framework.getName(), framework.getVersion())
                .then()
                .statusCode(201)
        ;
    }

    @Test
    @Order(50)
    void testGetAllAndBothPreviouslyCreatedAreReturned(){
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(2));
    }

    @Test
    @Order(55)
    void testPagination(){
        given().contentType(ContentType.JSON)
                .queryParam("page", "0")
                .queryParam("size", "1")
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(1));
    }

    @Test
    @Order(60)
    void testThatPreviouslySavedFrameworkCanBeLoadedByNameIgnoringCaseAndContainsAllFields() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks/{name}/{version}", jsFrameworkReact.getName().toLowerCase(), jsFrameworkReact.getVersion().toLowerCase())
                .then()
                .statusCode(200)
                .body("name", equalTo(jsFrameworkReact.getName()))
                .body("version", equalTo(jsFrameworkReact.getVersion()))
                .body("deprecationDate", equalTo(jsFrameworkReact.getDeprecationDate().toString()))
                .body("hypeLevel", equalTo(jsFrameworkReact.getHypeLevel()))
        ;
    }

    @Test
    @Order(70)
    void testUpdateExistingFramework() {
        JsFrameworkViewDTO framework = jsFrameworkWithValuesForUpdate;
        given().contentType(ContentType.JSON)
                .body(framework)
                .when()
                .put("/js-frameworks/{name}/{version}", jsFrameworkReact.getName(), jsFrameworkReact.getVersion())
                .then()
                .statusCode(204)
        ;
    }

    @Test
    @Order(80)
    void testThatPreviouslyUpdatedFrameworkWasUpdatedSuccessfully() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks/{name}/{version}", jsFrameworkReact.getName().toLowerCase(), jsFrameworkReact.getVersion().toLowerCase())
                .then()
                .statusCode(200)
                .body("name", equalTo(jsFrameworkReact.getName()))
                .body("version", equalTo(jsFrameworkReact.getVersion()))
                .body("deprecationDate", equalTo(jsFrameworkWithValuesForUpdate.getDeprecationDate().toString()))
                .body("hypeLevel", equalTo(jsFrameworkWithValuesForUpdate.getHypeLevel()))
        ;
    }

    @Test
    @Order(85)
    void testSearchByCriteria() {
        given().contentType(ContentType.JSON)
                .param("nameContains", "react")
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].name", equalTo(jsFrameworkReact.getName()))
                .body("[0].version", equalTo(jsFrameworkReact.getVersion()))
                .body("[0].deprecationDate", equalTo(jsFrameworkWithValuesForUpdate.getDeprecationDate().toString()))
                .body("[0].hypeLevel", equalTo(jsFrameworkWithValuesForUpdate.getHypeLevel()))
        ;
    }

    @Test
    @Order(90)
    void testDeleteFramework(){
        given().contentType(ContentType.JSON)
                .when()
                .delete("/js-frameworks/{name}/{version}", jsFrameworkReact.getName().toLowerCase(), jsFrameworkReact.getVersion().toLowerCase())
                .then()
                .statusCode(204)
        ;
    }

    @Test
    @Order(100)
    void testThatDeletedFrameworkIsReallyRemoved(){
        given().contentType(ContentType.JSON)
                .when()
                .get("/js-frameworks")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].name", equalTo(jsFrameworkJQuery.getName()))
        ;
    }
}
