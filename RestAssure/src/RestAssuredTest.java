import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    @BeforeClass
    public void setup() {
        // Ustawienie base URI dla API
        RestAssured.baseURI = "https://api.restful-api.dev";

        // Globalne ustawienie nagłówków (jeśli potrzebne)
        RestAssured.requestSpecification = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
    }

    @Test
    public void testGetObjectsEndpoint() {
        // Wysłanie żądania GET na endpoint /objects
        given()
                .log().all() // Logowanie żądania
                .when()
                .get("/objects")
                .then()
                .log().all() // Logowanie odpowiedzi
                .statusCode(200) // Oczekiwany kod odpowiedzi
                .contentType("application/json") // Sprawdzenie typu zawartości
                .body("size()", greaterThan(0)) // Sprawdzenie, czy lista nie jest pusta
                .body("id", everyItem(notNullValue())) // Sprawdzenie, czy wszystkie obiekty mają pole "id"
                .body("name", everyItem(notNullValue())) // Sprawdzenie, czy wszystkie obiekty mają pole "name"
                .body("data", everyItem(anyOf(nullValue(), notNullValue()))); // Sprawdzenie, czy pole "data" jest null lub nie puste
    }

    @DataProvider(name = "postData")
    public Object[][] createPostData() {
        return new Object[][] {
                { """
                {
                   "name": "Apple MacBook Pro 16",
                   "data": {
                      "year": 2019,
                      "price": 1849.99,
                      "CPU model": "Intel Core i9",
                      "Hard disk size": "1 TB"
                   }
                }
                """, "Apple MacBook Pro 16", 2019, 1849.99f, "Intel Core i9", "1 TB" },
                { """
                {
                   "name": "Apple iPhone 13",
                   "data": {
                      "year": 2021,
                      "price": 999.99,
                      "CPU model": "A15 Bionic",
                      "Hard disk size": "128 GB"
                   }
                }
                """, "Apple iPhone 13", 2021, 999.99f, "A15 Bionic", "128 GB" }
        };
    }

    @Test(dataProvider = "postData")
    public void testPostObject(String requestBody, String expectedName, int expectedYear, float expectedPrice, String expectedCpuModel, String expectedHardDiskSize) {
        // Wysłanie żądania POST na endpoint /objects
        given()
                .log().all() // Logowanie żądania
                .body(requestBody) // Dodanie ciała żądania
                .when()
                .post("/objects")
                .then()
                .log().all() // Logowanie odpowiedzi
                .statusCode(200) // Oczekiwany kod odpowiedzi
                .contentType("application/json") // Sprawdzenie typu zawartości
                .body("id", notNullValue()) // Sprawdzenie, czy odpowiedź zawiera pole "id"
                .body("name", equalTo(expectedName)) // Sprawdzenie, czy pole "name" ma oczekiwaną wartość
                .body("data.year", equalTo(expectedYear)) // Sprawdzenie, czy pole "year" ma oczekiwaną wartość
                .body("data.price", equalTo(expectedPrice)) // Sprawdzenie, czy pole "price" ma oczekiwaną wartość
                .body("data.'CPU model'", equalTo(expectedCpuModel)) // Sprawdzenie, czy pole "CPU model" ma oczekiwaną wartość
                .body("data.'Hard disk size'", equalTo(expectedHardDiskSize)); // Sprawdzenie, czy pole "Hard disk size" ma oczekiwaną wartość
    }
}

