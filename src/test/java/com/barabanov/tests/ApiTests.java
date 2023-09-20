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


    @Test
    public void addToCartTest(){
        String cookieValue = "41DB9E11694DABC6EBF2714400B4978FCA4CC0242ADDC96E2080FB265D2FFE913F5A7065564906A2B4C8D1B19065F24CD13C8566E888B29DC2E582DF05C2272AD2C3470AB40C5A943F12AA4E7581FB7C31CBC767CAF086FCAAF8EA0D374A2C886459CBA943F4D947CB1767A3B31CAD088FD77A6FEEB8AFBFE85921EDEAB98BCD64F8F9351AB0B1B89DE6BF714DDC37BA;",
                body = "product_attribute_75_5_31=96" +
                        "&product_attribute_75_6_32=100" +
                        "&product_attribute_75_3_33=102" +
                        "&product_attribute_75_8_35=108" +
                        "&addtocart_75.EnteredQuantity=2";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookieValue)
                .body(body)
                .when()
                .post("https://demowebshop.tricentis.com/addproducttocart/details/75/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("message", hasToString("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("success", is(true) );
    }

    @Test
    public void addToCartAnonymTest(){

                String body = "product_attribute_75_5_31=96" +
                        "&product_attribute_75_6_32=100" +
                        "&product_attribute_75_3_33=102" +
                        "&product_attribute_75_8_35=108" +
                        "&addtocart_75.EnteredQuantity=2";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(body)
                .when()
                .post("https://demowebshop.tricentis.com/addproducttocart/details/75/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("message", hasToString("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("success", is(true) )
                .body("updatetopcartsectionhtml", is("(2)"));
    }
}
