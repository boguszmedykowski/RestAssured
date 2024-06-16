Repozytorium zawiera testy api na stronie do nauki REST. Wykonuje podstawowe operacja, a następnie sprząta po sobie usuwając posty.


Feature:API Testing with RestAssured and Cucumber
Scenario:Get objects endpoint
    WhenI request the list of objects
    Thenthe response status code is 200
    Andthe response contains a list of objects
    
Scenario:Create a new object
    Givena new object with random data
    WhenI send a POST request to create the object
    Thenthe response status code is 200
    Andthe response contains the created object data
    
Scenario:Delete created objects
    Giventhe IDs of created objects
    WhenI send a DELETE request for each object
    Thenthe response status code is 200 for each delete
