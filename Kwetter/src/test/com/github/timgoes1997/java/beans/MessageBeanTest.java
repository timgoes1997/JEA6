package com.github.timgoes1997.java.beans;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import com.github.timgoes1997.java.dao.UserDAOImpl;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.MessageType;
import helper.PersistenceHelper;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class MessageBeanTest {
    private String token = "";
    private String username = "TimGoes2";
    private int id = 0;

    @Before
    public void setUp() throws Exception {
        Response registerResponse = given()
                .formParam("username", "TimGoes2")
                .formParam("password", "Wachtwoord")
                .formParam("email", "Ufkillsh0t2@gmail.com")
                .formParam("firstName", "Tim")
                .formParam("middleName", "")
                .formParam("lastName", "Goes")
                .formParam("telephone", "0627198715")
                .when()
                .post("http://localhost:8080/Kwetter/api/user/register");

        registerResponse.then().statusCode(200);

        token = registerResponse.getHeader("Authorization");

        id = given()
                .pathParam("username", username)
                .when()
                .get("http://localhost:8080/Kwetter/api/user/{username}")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @After
    public void tearDown() throws Exception {
        /*
        given()
                .header("Authorization", token)
                .pathParam("username", username)
                .when()
                .delete("http://localhost:8080/Kwetter/api/user/{username}/delete")
                .then()
                .statusCode(200);*/
    }

    @Test
    public void createAndGetMessageByID() {
        //Response response = given().pathParam("id", 1).when().get("http://localhost:8080/Kwetter/api/message/{id}");
        //String data = response.asString();
        //System.out.println("test " + data);
        int id = given()
                .header("Authorization", token)
                .formParam("message", "Dit is een geweldig bericht #geweldig @" + username)
                .formParam("messageType", MessageType.Public)
                .when()
                .post("http://localhost:8080/Kwetter/api/message/create")
                .then()
                .statusCode(200)
                .body("messager.username", equalTo(username))
                .extract()
                .path("id");


        given()
                .pathParam("id", id)
                .when()
                .get("http://localhost:8080/Kwetter/api/message/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id),
                        "messager.username", equalTo(username));
    }

    /*
    @Test
    public void postRegisterAccount() {
        System.out.println("test");
    }*/
}