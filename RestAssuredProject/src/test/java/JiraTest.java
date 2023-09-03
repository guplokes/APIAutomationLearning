import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		
		RestAssured.baseURI = "http://localhost:8080"; 
		
		//Session Filter
		SessionFilter session = new SessionFilter();
		
		//Login to Jira Rest Api scenario
		
		String response = given().log().all().header("Content-Type", "application/json")
		.body("{ \"username\": \"guplokes\", \"password\": \"lucky@2589\" }").filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().extract().response().asString();
		
		
		//log().all().assertThat().statusCode(DEFAULT_PORT)

		
		
		//ADDing comment to existing issue in jira
		String comment = "How are u?";
		
		String addCommentResponse  = given().pathParam("key", "RES-3").log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \""+comment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session)
		.when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(addCommentResponse);
		String commentId = js.getString("id");
		System.out.println(commentId);
		
		//adding attchment to existing issue in jira
		
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10002")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira.txt"))
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get ISsue
		
		String issueDetailsResponse = given().filter(session).pathParam("id", "10002")
				.queryParam("fields", "comment").log().all()
		.when().get("rest/api/2/issue/{id}")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = ReusableMethods.rawToJson(issueDetailsResponse);
		int commentsCount = js2.getInt("fields.comment.comments.size()");
		System.out.println(commentsCount);
		for(int i = 0; i < commentsCount; i++ ) {
			String issueCommentId = js2.get("fields.comment.comments[" + i + "].id");
			if(issueCommentId.equalsIgnoreCase(commentId)) {
				String msg = js2.get("fields.comment.comments[" + i + "].body");
				System.out.println(msg);
			}
		}
		

//		System.out.println(issueDetails);
	}

}
