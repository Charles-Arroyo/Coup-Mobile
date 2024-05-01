package database;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import database.Users.User;
import database.Users.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BoTestingSystemTest {

	@Autowired
	UserRepository userRepository;
	User user;

  @LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	/**
	 * Teest to see if you can
	 * send a friend request to
	 * non-existent user
	 */
	@Test
	public void trySendingNonExistentUserFriendRequest() {
		// Get all users from the repository
		List<User> users = userRepository.findAll();

		String nonExistentUserEmail = "nonexistent@example.com";

		if (users.size() >= 1) {
			User existingUser = users.get(0);
			String existingUserEmail = existingUser.getUserEmail();

			// Send POST request to send a friend request from a non-existent user to an existing user
			Response response = RestAssured.given()
					.header("Content-Type", "text/plain")
					.header("charset", "utf-8")
					.pathParam("userEmail", nonExistentUserEmail)
					.pathParam("friendWannaBe", existingUserEmail)
					.when()
					.post("/sendRequest/{userEmail}/{friendWannaBe}");

			// Check status code
			int statusCode = response.statusCode();
			assertEquals(200, statusCode);

			// Check response body for correct response
			Map<String, Object> responseBody = response.jsonPath().getMap("");

			// Check if the response indicates failure
			boolean success = (boolean) responseBody.get("success");
			assertFalse(success);

			// Check the error message
			String message = (String) responseBody.get("message");
			assertEquals("One or both users do not exist", message);
		} else {
			fail("No users found in the repository to perform the test.");
		}
	}


	/**
	 * Try deleting relationships that
	 * doesn't exit and if it doesn't allow
	 * then it works correctly
	 */
	@Test
	public void tryDeleteNonExistentFriendship() {
		List<User> users = userRepository.findAll();

		if (users.size() >= 2) {
			User user1 = users.get(0);
			User user2 = users.get(1);

			String userEmail1 = user1.getUserEmail();
			String userEmail2 = user2.getUserEmail();

			// Send DELETE request to delete a non-existent friendship
			Response response = RestAssured.given()
					.header("Content-Type", "text/plain")
					.header("charset", "utf-8")
					.pathParam("userEmail", userEmail1)
					.pathParam("userEmail2", userEmail2)
					.when()
					.delete("/deleteFriend/{userEmail}/{userEmail2}");

			// Check status code
			int statusCode = response.statusCode();
			assertEquals(400, statusCode);

			// Check response body for correct response
			Map<String, Object> responseBody = response.jsonPath().getMap("");

			// Check if the response indicates failure
			boolean success = (boolean) responseBody.get("success");
			assertFalse(success);

			// Check the error message
			String message = (String) responseBody.get("message");
			assertEquals("Friendship does not exist", message);
		} else {
			fail("Not enough users in the repository to perform the test.");
		}
	}

	/**
	 * This test is to see if you can reset
	 * a non-existent user's score
	 */
	@Test
	public void tryResetNonExistentUserScore() {
		// Generate a random non-existent user email
		String nonExistentUserEmail = "nonexistent@example.com";

		// Send PUT request to reset the score of the non-existent user
		Response response = RestAssured.given()
				.header("Content-Type", "text/plain")
				.header("charset", "utf-8")
				.pathParam("userEmail", nonExistentUserEmail)
				.when()
				.put("/resetScore/{userEmail}");

		// Check status code
		int statusCode = response.statusCode();
		assertEquals(404, statusCode);

		// Check response body for correct response
		String responseBody = response.getBody().asString();
		assertEquals("{\"error\":\"User not found\"}", responseBody);
	}

	/**
	 * Test to accept a friend request
	 * between two non-existent users
	 */
	@Test
	public void acceptFriendRequestBetweenTwoNonExistentUsers() {
		String nonExistentUserEmail1 = "nonexistent1@example.com";
		String nonExistentUserEmail2 = "nonexistent2@example.com";
		boolean yesOrNo = true;

		// Send POST request to accept a friend request between two non-existent users
		Response response = RestAssured.given()
				.header("Content-Type", "text/plain")
				.header("charset", "utf-8")
				.pathParam("yesOrNo", yesOrNo)
				.pathParam("userEmail", nonExistentUserEmail1)
				.pathParam("askingPerson", nonExistentUserEmail2)
				.when()
				.post("/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}");

		// Check status code
		int statusCode = response.statusCode();
		assertEquals(200, statusCode);

		// Check response body for correct response
		Map<String, Object> responseBody = response.jsonPath().getMap("");

		// Check if the response indicates failure
		boolean success = (boolean) responseBody.get("success");
		assertFalse(success);

		// Check the error message
		String message = (String) responseBody.get("message");
		assertEquals("Friend request not found", message);
	}

}
