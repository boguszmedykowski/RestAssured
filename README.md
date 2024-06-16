# RestAssured Cucumber API Testing

Projekt do testowania API przy użyciu RestAssured i Cucumber.

## Wymagania

- Java 15 lub nowsza
- Maven 3.6.3 lub nowszy
- IntelliJ IDEA (opcjonalnie)

## Uruchomienie

1. Sklonuj repozytorium:
    ```sh
    git clone https://github.com/boguszmedykowski/RestAssured.git
    ```
2.  Uruchom plik RestAssure/src/test/java/RunCucumberTest.java



# API Testing with RestAssured and Cucumber

## Scenario: Get objects endpoint
- **When** I request the list of objects
- **Then** the response status code is 200
- **And** the response contains a list of objects

## Scenario: Create a new object
- **Given** a new object with random data
- **When** I send a POST request to create the object
- **Then** the response status code is 200
- **And** the response contains the created object data

## Scenario: Delete created objects
- **Given** the IDs of created objects
- **When** I send a DELETE request for each object
- **Then** the response status code is 200 for each delete

