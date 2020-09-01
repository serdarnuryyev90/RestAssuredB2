package goRest;

import goRest.pojo.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestTests {

    private int userId;

    @Test(enabled = false)
    public void extractingListOfUsers() {
        List<User> userList = given()
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                .extract().response().jsonPath().getList("data", User.class);

        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test(enabled = false)
    public void extractingListOfUsersAsArray() {
        User[] userList = given()
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                .extract().response().jsonPath().getObject("data", User[].class);

        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test(enabled = false)
    public void extractingOneUser() {
        User user = given()
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                .extract().response().jsonPath().getObject("data[2]", User.class);

        System.out.println(user);
    }

    @Test
    public void creatingUser() {
        userId = given()
                // specify Authorization header, body, Content-Type header
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + randomEmail() + "\", \"name\": \"Techno\", \"gender\":\"Male\", \"status\": \"Active\"}")
                .when()
                .post("https://gorest.co.in/public-api/users")
                .then()
                .statusCode(200)
                .body("code", equalTo(201))
                .extract().response().jsonPath().getInt("data.id");
    }

    @Test(dependsOnMethods = "creatingUser")
    public void getUserById() {
        given()
                .when()
                .get("https://gorest.co.in/public-api/users/" + userId)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("data.id", equalTo(userId))
        ;
    }

    @Test(dependsOnMethods = "creatingUser")
    public void updateUserById() {
        String updateString = "Techno Study";
        given()
                // specify Authorization header, body, Content-Type header
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"name\": \"" + updateString + "\"}")
                .when()
                .put("https://gorest.co.in/public-api/users/" + userId)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("data.name", equalTo(updateString))
        ;
    }

    @Test(dependsOnMethods = "creatingUser")
    public void deleteUserById() {
        given()
                // specify Authorization header, body, Content-Type header
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .when()
                .delete("https://gorest.co.in/public-api/users/" + userId)
                .then()
                .statusCode(200)
                .body("code", equalTo(204))
        ;
    }

    private String randomEmail() {
        return RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
    }

}
