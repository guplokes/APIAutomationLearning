package stepDefinations;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.ApiResources;
import resources.TestDataBuild;
import resources.Utils;

public class StepDefination extends Utils {
	ResponseSpecification resspec;
	RequestSpecification req;
	Response response;
	TestDataBuild data = new TestDataBuild();
	static String  place_Id;
	
//	@Given("Add Place Payload")
//	public void add_place_payload() throws IOException {
//	    // Write code here that turns the phrase above into concrete actions
//		
//
//
//		
//		res = given().spec(this.requestSpecification())
//				.body(data.addPlacePayload()); 
//	}
	
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_place_payload_with(String name, String language, String address) throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		req = given().spec(this.requestSpecification())
				.body(data.addPlacePayload(name, language, address)); 
	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String operation) {
		// Write code here that turns the phrase above into concrete
		//Constructor will be called with value Of resource which you pass
		ApiResources resourceAPI = ApiResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());

		resspec = new ResponseSpecBuilder().expectStatusCode(200) 
				.expectContentType(ContentType.JSON).build();
		
		if(operation.equalsIgnoreCase("POST"))
			response = req.when().post(resourceAPI.getResource());
		else if(operation.equalsIgnoreCase("GET"))
			response = req.when().get(resourceAPI.getResource());

//		response = res.when().post(resourceAPI.getResource()).then().spec(resspec).extract().response();
	}
	
	@Then("api call got success with status code {int}")
	public void api_call_got_success_with_status_code(Integer statusCode) {
	    // Write code here that turns the phrase above into concrete actions
//		resspec = new ResponseSpecBuilder().expectStatusCode(statusCode)
//					.expectContentType(ContentType.JSON).build();
		
	    assertEquals(response.statusCode(),200);
	}
	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyValue, String expectedValue) {
	    // Write code here that turns the phrase above into concrete actions
	    
	    assertEquals(this.getJsonPath(response, keyValue),expectedValue);
	     
	}
	
	@Then("verify place_Id created maps to {string} using {string}")
	public void verify_place_id_created_maps_to_using(String expectedName, String resource) throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		place_Id = this.getJsonPath(response, "place_id");
		req = given().spec(this.requestSpecification()).queryParam("place_id", place_Id);
		user_calls_with_http_request(resource, "GET");
		String actualName = this.getJsonPath(response, "name");
		assertEquals(actualName, expectedName);
		
	    
	}
	
	@Given("DeletePlace Payload")
	public void delete_place_payload() throws IOException {
	    // Write code here that turns the phrase above into concrete actions
	  req =  given().spec(this.requestSpecification())
	    .body(data.deletePayload(place_Id));
	}

}
