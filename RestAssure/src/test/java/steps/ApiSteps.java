package steps;

import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiSteps {

    private Response response;
    private Faker faker;
    private List<String> createdIds = new ArrayList<>();
    private String requestBody; // Przechowuje ciało żądania

    public ApiSteps() {
        RestAssured.baseURI = "https://api.restful-api.dev";
        RestAssured.requestSpecification = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");
        faker = new Faker(Locale.ENGLISH);
    }

    @When("I request the list of objects")
    public void i_request_the_list_of_objects() {
        response = given()
                .log().all()
                .when()
                .get("/objects");
    }

    @Then("the response status code is {int}")
    public void the_response_status_code_is(Integer statusCode) {
        response.then().log().all().statusCode(statusCode);
    }

    @Then("the response contains a list of objects")
    public void the_response_contains_a_list_of_objects() {
        response.then().body("size()", greaterThan(0))
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("data", everyItem(anyOf(nullValue(), notNullValue())));
    }

    @Given("a new object with random data")
    public void a_new_object_with_random_data() {
        String name = faker.commerce().productName();
        int year = faker.number().numberBetween(2000, 2024);
        double price = faker.number().randomDouble(2, 100, 5000);
        String cpuModel = faker.commerce().material();
        String hardDiskSize = faker.number().numberBetween(128, 2000) + " GB";
        String formattedPrice = String.format(Locale.ENGLISH, "%.2f", price);

        requestBody = String.format(Locale.ENGLISH, """
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
    }

    @When("I send a POST request to create the object")
    public void i_send_a_post_request_to_create_the_object() {
        response = given()
                .log().all()
                .body(requestBody)
                .when()
                .post("/objects");

        String id = response.then()
                .log().all()
                .extract().path("id");
        createdIds.add(id);
    }

    @Then("the response contains the created object data")
    public void the_response_contains_the_created_object_data() {
        response.then()
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("data.year", notNullValue())
                .body("data.price", notNullValue())
                .body("data.'CPU model'", notNullValue())
                .body("data.'Hard disk size'", notNullValue());
    }

    @Given("the IDs of created objects")
    public void the_IDs_of_created_objects() {
        // No action needed, as IDs are already stored in createdIds
    }

    @When("I send a DELETE request for each object")
    public void i_send_a_DELETE_request_for_each_object() {
        for (String id : createdIds) {
            response = given()
                    .log().all()
                    .when()
                    .delete("/objects/" + id);

            // Sprawdzenie odpowiedzi DELETE dla każdego obiektu
            response.then().log().all().statusCode(200);
        }
    }

    @Then("the response status code is 200 for each delete")
    public void the_response_status_code_is_200_for_each_delete() {
        // Metoda już wykonuje sprawdzenie statusu dla każdego usunięcia w pętli `for`
    }
}
