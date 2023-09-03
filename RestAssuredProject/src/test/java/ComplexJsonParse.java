import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Mocking the respnse here without the API Present
		JsonPath js = new JsonPath(Payload.course());
		
		//1. Print No of courses returned by API

		int count = js.getInt("courses.size()");
		System.out.println(count);
		
//		2.Print Purchase Amount
		System.out.println(js.getString("dashboard.purchaseAmount"));
		
//		3. Print Title of the first course
		System.out.println(js.getString("courses[0].title"));
		System.out.println(js.getString("courses[2].title"));
		System.out.println("--------------------------------");
		
//		4. Print All course titles and their respective Prices
		
		for(int i = 0; i < count; i++) {
			System.out.println(js.getString("courses[" + i + "].title"));
			System.out.println(js.getString("courses[" + i + "].price"));
		}
		System.out.println("--------------------------------");
		
//		5. Print no of copies sold by RPA Course
		for(int i = 0; i < count; i++) {
			String title = js.getString("courses[" + i + "].title");
			if(title.equals("RPA")) {
				System.out.println(js.getString("courses[" + i + "].copies"));
				break;
			}
			
		}
		System.out.println("--------------------------------");
		
//		6. Verify if Sum of all Course prices matches with Purchase Amount 
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		int sum = 0;
		for(int i = 0; i < count; i++) {
			sum = sum + js.getInt("courses[" + i + "].price") * js.getInt("courses[" + i + "].copies");
		}
		System.out.println(purchaseAmount);
		System.out.println(sum);
		if(sum == purchaseAmount)
			System.out.println("true");
		else
			System.out.println("false");
	}

}
