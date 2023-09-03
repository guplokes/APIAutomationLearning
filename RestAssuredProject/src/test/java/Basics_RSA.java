import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;

public class Basics_RSA {
	//RSA- Rahul shetty Academy
	public static void main(String[] args) {	

			// validate if Add Place API is workimg as expected 
			
			
			//given - all input details 
			//when - Submit the API -resource,http method
			//Then - validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
//		given().log().all().queryParam("key","qaclick123").header("Content-Type", "application/json")
//		.body(Payload.addPlace())
//		.when().post("maps/api/place/add/json")
//		.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
//		.header("Server", "Apache/2.4.41 (Ubuntu)");
//		
		//Add place-> Update Place with New Address -> Get Place to validate if New address is present in response
		
		String response = given().log().all().queryParam("key","qaclick123").header("Content-Type", "application/json")
							.body(Payload.addPlace())
							.when().post("maps/api/place/add/json")
							.then().assertThat().statusCode(200).extract().response().asString();
		System.out.println(response);
		
		JsonPath js = new JsonPath(response); //For Parsing into json
		String placeId = js.getString("place_id");
		
//		System.out.println(placeId);
		
		//Update Place
		String newAddress = "gali no 2, Sarffabad";
		
		given().log().all().queryParam("key","qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//Get Palce
		
		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = new JsonPath(getResponse);
		String actualAddress = js2.getString("address");
		Assert.assertEquals(actualAddress, newAddress);
		System.out.println(actualAddress);
		
	}
}
