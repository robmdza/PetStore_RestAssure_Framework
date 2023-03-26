Feature: Validating Petstore "pet" APIs

Scenario: Verify if an image can be uploaded for a pet
  Given Add image to pet record
  When user calls "uploadImage" API with POST HTTP request using "12" as pet_id
  Then user receives the http "200" code
  And user receives correct API response

Scenario: Verify if a new pet can be added to the store
  Given a new pet to be added to the store
  When user calls "pet" API with POST HTTP request and body payload
  Then user receives the http "200" code for adding pet
  And user receives correct API Body response that cointains an ID

  Scenario: Verify GET pet by ID
    Given Get a pet by id: "2000"
    When user calls "pet" API with GET HTTP request and pet_id
    Then user receives the http "200" code for GET pet
    And user receives correct API Body response that contains the correct pet

#Great Example
#Feature: Test GET request
#
#  Scenario: Verify GET request
#    Given the base URI is "https://jsonplaceholder.typicode.com"
#    When a GET request is made to "/posts/1"
#    Then the status code should be 200
#    And the response body should contain "userId"