import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import files.ReusableMethods;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class OauthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String accessToken;

//		WebDriverManager.chromedriver().setup();
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("lokeshvarshney786@gmail.com");
//		driver.findElement(By.xpath("//span[text()='Next']")).click();

		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh62FM36WdFIBV5CCQGUdIcJa5zyUuZuFBghrLTEGDozWy8f9D0n514zRmwKi-5AHw&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partialcode = url.split("code=")[1];

		String code = partialcode.split("&scope")[0];

		System.out.println(code);

		// GetAccessToken Request :
		String accessTokenResponse = given().urlEncodingEnabled(false).queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when()
				.post("https://www.googleapis.com/oauth2/v4/token").then().log().all().extract().response().asString();

		JsonPath js = ReusableMethods.rawToJson(accessTokenResponse);
		accessToken = js.getString("access_token");

//		String response = given().contentType("application/json")
//				.queryParams("access_token", accessToken)
//		.when().get("https://rahulshettyacademy.com/getCourse.php")
//		.then().log().all().extract().response().asString();

		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).when()
				.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

		System.out.println(gc.getExpertise());
//		
		System.out.println(gc.getLinkedIn());

		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		List<Api> apiCourses = gc.getCourses().getApi();

		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equals("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
		}

		List<WebAutomation> webCourses = gc.getCourses().getWebAutomation();

		for (int i = 0; i < webCourses.size(); i++) {
				System.out.println(webCourses.get(i).getCourseTitle());
		}

//		System.out.println(response);

	}

}
