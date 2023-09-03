import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethods;

public class DynamicJson {

	@Test(dataProvider = "BooksData")
	public void addBookTest(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = 	given().header("Content-Type", "application/json")
							.body(Payload.addBook(isbn, aisle))
							.when().post("Library/Addbook.php")
							.then().log().all().assertThat().statusCode(200)
							.extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.getString("ID");
		System.out.println(id);
		
	}
	
	@Test(dataProvider = "BooksData")
	public void deleteBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String id = isbn + aisle;
		String response = given().header("Content-Type", "application/json")
				.body(Payload.deleteBook(id))
				.when().post("Library/DeleteBook.php")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
				
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		Object[][] booksArray = {{"fhtf","4657"}, {"ascnl","6329"}, {"kmqa","1987"}};
		return booksArray;
	}
}
