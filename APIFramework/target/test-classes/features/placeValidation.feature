Feature: Validating Place API's

@AddPlace
Scenario Outline: Verify if Place is bieng successfully added using Add Place api
	Given Add Place Payload with "<name>" "<language>" "<address>"
	When user calls "addPlaceAPI" with "Post" http request
	Then api call got success with status code 200
	And "status" in response body is "OK"
	And "scope" in response body is "APP"
	And verify place_Id created maps to "<name>" using "getPlaceAPI"

 Examples: 
 |name    |language|address         |
 |House-C6|English |A Square Mall   |
# |BBHouse |Spanish |Sea CRoss Centre|

@DeletePlace
Scenario: Verify if Delete Place functionality id working
	Given DeletePlace Payload
	When user calls "deletePlaceAPI" with "Post" http request
	Then api call got success with status code 200
	And "status" in response body is "OK"
	
