package stepDefinations;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {

	@Before("@DeletePalce")
	public void beforeScenario() throws IOException {
		
		StepDefination sd = new StepDefination();
		if(StepDefination.place_Id == null) {
		sd.add_place_payload_with("Keshav Bhawan", "Braj", "Bank road");
		sd.user_calls_with_http_request("addPlaceAPI", "POST");
		sd.verify_place_id_created_maps_to_using("Keshav Bhawan", "getPlaceAPI");
		}
	}
}
