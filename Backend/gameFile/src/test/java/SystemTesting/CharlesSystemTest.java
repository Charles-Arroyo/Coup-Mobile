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
    public void getSignInLogTest() {
        Response response = RestAssured.given()
                .when()
                .get("/getsignLog/pizza");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("signInLogs", not(empty()));
    }

    @Test
    public void checkUserStatusTestNonActiveUser() {
        Response response = RestAssured.given()
                .when()
                .get("/checkUserStatus/cat");

        response.then()
                .assertThat()
                .statusCode(200);
    }

    /**
     * Test for signing up with a used username
     */

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

    /**
     * Test for sigining in with account
     */
    @Test
    public void signInTestExistingUser(){
        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("password", "123");
        requestBody.put("userEmail", "zat");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/signin");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    /**
     * Test for signing in without an account
     */
    @Test
    public void signInTestNonExistingUser(){
        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("password", "123");
        requestBody.put("userEmail", "NotReal");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/signin");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("fail", equalTo(false));
    }

    }



