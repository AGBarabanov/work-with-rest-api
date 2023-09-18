package com.barabanov.tests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ApiTests {
    /*
        1. Делаем запрос к https://reqres.in/api/users/2
        2. Получакм ответ
        {
            "data": {
                "id": 2,
                "email": "janet.weaver@reqres.in",
                "first_name": "Janet",
                "last_name": "Weaver",
                "avatar": "https://reqres.in/img/faces/2-image.jpg"
            },
            "support": {
                "url": "https://reqres.in/#support-heading",
                 "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
            }
           }
           3. Смотрим, что "first_name" = "Janet"
    */

    @Test
    public void checkFirstName(){
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", equalTo("Janet"));
    }

    @Test
    public void checkFirstNameWithLogs(){
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                //.log().all()                                        Получаем все логи после запроса
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", equalTo("Janet"));
    }

    @Test
    public void checkColor(){
        String expectedColor = "#C74375";

       given()
               .when()
               .get("https://reqres.in/api/unknown")
               .then()
               .log().status()
               .log().body()
               .statusCode(200)
               .body("data.color", hasItem("#C74375"));
    }

    @Test
    public void equalsTotal(){
        Integer ExpectedTotal = 12;

        Integer actualTotal = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("total");

        assertEquals(ExpectedTotal, actualTotal);
    }

    @Test
    public void checkTotalWithAuth(){
        given()
                .auth().basic("eve.holt@reqres.in", "cityslicka")
                .when()
                .get("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total_pages", is(2));
    }

    @Test
    public void loginTest(){
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
}
