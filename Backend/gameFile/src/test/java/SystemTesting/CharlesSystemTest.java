package SystemTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import database.Stats.Stat;
import database.Stats.StatRepository;
import database.Users.User;
import database.Users.UserRepository;
import io.restassured.parsing.Parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import database.Main;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CharlesSystemTest {

    @LocalServerPort
    int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatRepository statRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.defaultParser = Parser.JSON; // Set default parser as JSON
        RestAssured.registerParser("text/plain", Parser.JSON); // Register 'text/plain' to be parsed as JSON
    }

    @Test
    public void signUpTestExistingUser() {
        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("password", "123");
        requestBody.put("userEmail", "zat");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/signup");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("fail", equalTo(false));
    }

    }
//
//
//    @Test
//    public void testSignUpCreatesUserAndStats() {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("email", "newuser@example.com");
//        requestBody.put("password", "password123");
//
//        // Assume the user does not exist yet
//        Mockito.when(userRepository.findByUserEmail("newuser@example.com")).thenReturn(null);
//
//        // Assume saving user and stats works correctly
//        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
//        Mockito.when(statRepository.save(any(Stat.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        // Send request
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .body(requestBody.toString())
//                .when()
//                .post("/signup");
//
//        // Assert the response was successful
//        response.then()
//                .assertThat()
//                .statusCode(200)
//                .body("success", equalTo(true));
//
//        // Verify the user was saved with stats
//        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//        Mockito.verify(userRepository).save(userCaptor.capture());
//        User savedUser = userCaptor.getValue();
//
//        assertNotNull(savedUser.getStat());
//        assertNotNull(savedUser.getStat()); // Ensure ID is set, indicating it was saved
//
//        // Verify the stat repository was used
//        Mockito.verify(statRepository).save(any(Stat.class));
//    }
//
//
//
//}
