package com.barabanov.tests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
        given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.color", hasKey("#C74375"));
    }
}
