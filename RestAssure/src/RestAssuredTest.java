import org.testng.annotations.Test;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    @Test
    public void testGetPostsEndpoint() {
        // Ustawienie base URI dla API
        RestAssured.baseURI = "https://api.restful-api.dev";

        // Wysłanie żądania GET na endpoint /posts
        given()
                .when()
                .get("/objects")
                .then()
                .statusCode(200) // Oczekiwany kod odpowiedzi
                .body("data", notNullValue()) // Sprawdzenie, czy pole "data" nie jest puste
                .body("data.size()", greaterThan(0)); // Sprawdzenie, czy lista postów nie jest pusta
    }
}
