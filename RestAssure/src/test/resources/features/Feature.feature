Feature: API Testing with RestAssured and Cucumber

  Scenario: Get objects endpoint
    When I request the list of objects
    Then the response status code is 200
    And the response contains a list of objects

  Scenario: Create a new object
    Given a new object with random data
    When I send a POST request to create the object
    Then the response status code is 200
    And the response contains the created object data

  Scenario: Delete created objects
    Given the IDs of created objects
    When I send a DELETE request for each object
    Then the response status code is 200 for each delete
