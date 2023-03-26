package org.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.GsonMapper;
import io.restassured.mapper.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.pojo.AddPetPayload;
import org.pojo.UploadImageResponse;
import io.restassured.RestAssured.*;
import java.io.File;
import com.google.gson.Gson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@CucumberOptions(features="src/main/java/org/features", glue={"stepDefinitions"})
@RunWith(Cucumber.class) //test

public class stepDefinition {
    private static RequestSpecBuilder request_SB; // SB = SpecBuilder.
    //ResponseSpecBuilder response_SB;
    private UploadImageResponse response_SB;
    private Response response;
    private AddPetPayload petPayload;


    static{

        request_SB = new RequestSpecBuilder();
        request_SB.setBaseUri("https://petstore.swagger.io/");
    }

    public stepDefinition() {
    }

    @Given("Add image to pet record")
    public void add_image() {
        // Write code here that turns the phrase above into concrete actions
        request_SB.addMultiPart("additionalMetadata", "cute image");
        request_SB.addMultiPart(new File("src/main/resources/pet_image.jpg"));
        request_SB.setContentType(ContentType.MULTIPART);
        request_SB.setAccept(ContentType.JSON);
        //throw new io.cucumber.java.PendingException();
    }
    @When("user calls {string} API with POST HTTP request using {string} as pet_id")
    public void user_calls_api_with_post_http_request_using_as_pet_id(String uploadImage, String pet_id) {
        // Write code here that turns the phrase above into concrete actions
        response_SB =
                given()
                        .spec(request_SB.build())
                        .log().all()
                        .pathParam("pet_id", pet_id)
                        .pathParam("api_call", uploadImage)
                        .when()
                        .post("v2/pet/{pet_id}/{api_call}")
                        .as(UploadImageResponse.class);
        System.out.println("---->>"+response_SB.getCode());
        //throw new io.cucumber.java.PendingException();
    }
    @Then("user receives the http {string} code")
    public void user_receives_the_http_code(String code) {
        // Write code here that turns the phrase above into concrete actions
         assertEquals(response_SB.getCode(), code);
        System.out.println("Code: "+ code + " POJO: "+response_SB.getCode());
        //throw new io.cucumber.java.PendingException();
    }
    @Then("user receives correct API response")
    public void user_receives_correct_api_response() {
        // Write code here that turns the phrase above into concrete actions
        //assertEquals(response_SB.getMessage(), );
        System.out.println(response_SB.getMessage());
        //throw new io.cucumber.java.PendingException();
    }
//Scenario #2 -------------
    @Given("a new pet to be added to the store")
    public void a_new_pet_to_be_added_to_the_store() {
        request_SB.setContentType(ContentType.JSON);
        request_SB.setAccept(ContentType.JSON);
    }
    @When("user calls {string} API with POST HTTP request and body payload")
    public void user_calls_api_with_post_http_request_and_body_payload(String string) {
       response =
               given()
                       .spec(request_SB.build())
                       .body("{\n" +
                               "  \"id\": 2000,\n" +
                               "  \"category\": {\n" +
                               "    \"id\": 5,\n" +
                               "    \"name\": \"Wild\"\n" +
                               "  },\n" +
                               "  \"name\": \"Dingo\",\n" +
                               "  \"photoUrls\": [\n" +
                               "    \"string\"\n" +
                               "  ],\n" +
                               "  \"tags\": [\n" +
                               "    {\n" +
                               "      \"id\": 0,\n" +
                               "      \"name\": \"string\"\n" +
                               "    }\n" +
                               "  ],\n" +
                               "  \"status\": \"available\"\n" +
                               "}")
                       .when()
                       .post("/v2/"+string);
        System.out.println("--------->"+ response.getBody().asString());
    }
    @Then("user receives the http {string} code for adding pet")
    public void user_receives_the_http_code_for_adding_pet(String string) {
        assertEquals(String.valueOf(response.getStatusCode()), string);
    }
    @Then("user receives correct API Body response that cointains an ID")
    public void user_receives_correct_api_body_response_that_cointains_an_id() {
        //System.out.println("Response id: "+response.jsonPath().getString("id"));
    //Assert.assertTrue (response.jsonPath().getString("id") != "0");
    }

    //GET Pet by ID
    @Given("Get a pet by id: {string}")
    public void get_a_pet_by_id(String string) {
        request_SB.addPathParams("pet_id", string)
                .setContentType(ContentType.JSON);
                //.build();
    }
    @When("user calls {string} API with GET HTTP request and pet_id")
    public void user_calls_api_with_get_http_request_and_pet_id(String string) {
        petPayload =
                given()
                        .log().all()
                        .spec(request_SB.build())
                        .when()
                        .get("v2/pet/{pet_id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AddPetPayload.class);
    }
    @Then("user receives the http {string} code for GET pet")
    public void user_receives_the_http_code_for_get_pet(String string) {
        System.out.println(petPayload.toString());
    }
    @Then("user receives correct API Body response that contains the correct pet")
    public void user_receives_correct_api_body_response_that_contains_the_correct_pet() {
        Gson mapper = new Gson();
        String petPayloadJSON = mapper.toJson(petPayload);
        System.out.println(petPayloadJSON);
    }

}
