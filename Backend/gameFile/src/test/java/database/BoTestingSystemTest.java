package database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import database.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BoTestingSystemTest {

	User user;

  @LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	@Test
	public void adminGetAllUser(){
		//Send GET request and get all the users
		Response response = RestAssured.given().
				header("Content-Type", "text/plain").
				header("charset","utf-8").
				body("admin").
				when().
				get("/users");
		// check status code
		int statusCode = response.statusCode();
		assertEquals(200, statusCode);

		// Check response body for correct response
		String returnString = response.getBody().asString();
		try {
			JSONArray returnArr = new JSONArray(returnString);
			JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
			assertEquals(user, returnObj.get("data"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void reverseTest() {
//		// Send request and receive response
//		Response response = RestAssured.given().
//				header("Content-Type", "text/plain").
//				header("charset","utf-8").
//				body("hello").
//				when().
//				post("/reverse");
//
//
//		// Check status code
//		int statusCode = response.getStatusCode();
//		assertEquals(200, statusCode);
//
//		// Check response body for correct response
//		String returnString = response.getBody().asString();
//		try {
//			JSONArray returnArr = new JSONArray(returnString);
//			JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//			assertEquals("olleh", returnObj.get("data"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void capitalizeTest() {
//		// Send request and receive response
//		Response response = RestAssured.given().
//				header("Content-Type", "text/plain").
//				header("charset","utf-8").
//				body("hello").
//				when().
//				post("/capitalize");
//
//
//		// Check status code
//		int statusCode = response.getStatusCode();
//		assertEquals(200, statusCode);
//
//		// Check response body for correct response
//		String returnString = response.getBody().asString();
//		try {
//			JSONArray returnArr = new JSONArray(returnString);
//			JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//			assertEquals("HELLO", returnObj.get("data"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void reverseMultipleInputsTest() {
//		String[] inputs = {"hello", "world", "foo", "bar"};
//		String[] expectedOutputs = {"olleh", "dlrow", "oof", "rab"};
//
//		for (int i = 0; i < inputs.length; i++) {
//			Response response = RestAssured.given().
//					header("Content-Type", "text/plain").
//					header("charset","utf-8").
//					body(inputs[i]).
//					when().
//					post("/reverse");
//
//			int statusCode = response.getStatusCode();
//			assertEquals(200, statusCode);
//
//			String returnString = response.getBody().asString();
//			try {
//				JSONArray returnArr = new JSONArray(returnString);
//				JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//				assertEquals(expectedOutputs[i], returnObj.get("data"));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Test
//	public void reverseEmptyStringTest() {
//		Response response = RestAssured.given().
//				header("Content-Type", "text/plain").
//				header("charset","utf-8").
//				body("").
//				when().
//				post("/reverse");
//
//		int statusCode = response.getStatusCode();
//		assertEquals(200, statusCode);
//
//		String returnString = response.getBody().asString();
//		try {
//			JSONArray returnArr = new JSONArray(returnString);
//			JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//			assertEquals("", returnObj.get("data"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void reverseNullInputTest() {
//		Response response = RestAssured.given().
//				header("Content-Type", "text/plain").
//				header("charset","utf-8").
//				when().
//				post("/reverse");
//
//		int statusCode = response.getStatusCode();
//		assertEquals(400, statusCode); // Assuming 400 Bad Request is returned
//	}
}
