import com.github.javafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.RestAssured;

import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    private Faker faker;

    @BeforeClass
    public void setup() {
        // Ustawienie base URI dla API
        RestAssured.baseURI = "https://api.restful-api.dev";

        // Globalne ustawienie nagłówków (jeśli potrzebne)
        RestAssured.requestSpecification = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        // Inicjalizacja Faker z angielską lokalizacją
        faker = new Faker(new Locale("en"));
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
                generateRandomData(),
                generateRandomData()
        };
    }

    private Object[] generateRandomData() {
        String name = faker.commerce().productName();
        int year = faker.number().numberBetween(2000, 2024);
        double price = faker.number().randomDouble(2, 100, 5000);
        String cpuModel = faker.commerce().material();
        String hardDiskSize = faker.number().numberBetween(128, 2000) + " GB";

        // Formatowanie ceny z kropką jako separatorem dziesiętnym
        String formattedPrice = String.format(Locale.ENGLISH, "%.2f", price);

        String requestBody = String.format(Locale.ENGLISH, """
            {
               "name": "%s",
               "data": {
                  "year": %d,
                  "price": %s,
                  "CPU model": "%s",
                  "Hard disk size": "%s"
               }
            }
            """, name, year, formattedPrice, cpuModel, hardDiskSize);

        return new Object[] { requestBody, name, year, price, cpuModel, hardDiskSize };
    }

    @Test(dataProvider = "postData")
    public void testPostObject(String requestBody, String expectedName, int expectedYear, double expectedPrice, String expectedCpuModel, String expectedHardDiskSize) {
        // Wysłanie żądania POST na endpoint /objects
        System.out.println("Sending POST request with body: " + requestBody);

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
                .body("data.price", equalTo((float) expectedPrice)) // Sprawdzenie, czy pole "price" ma oczekiwaną wartość
                .body("data.'CPU model'", equalTo(expectedCpuModel)) // Sprawdzenie, czy pole "CPU model" ma oczekiwaną wartość
                .body("data.'Hard disk size'", equalTo(expectedHardDiskSize)); // Sprawdzenie, czy pole "Hard disk size" ma oczekiwaną wartość
    }
}
