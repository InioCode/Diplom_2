package client.createuser;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Random;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.REGISTRATION_ENDPOINT;

public class CreateUser {
    public static boolean userCreated = false;

    public static String generateRandomEmail(){
        return "mail" + new Random().nextInt(1000) +"@mail.ru";
    }
    public static String generateRandomEmail(String emailName){
        return emailName + new Random().nextInt(1000) +"@mail.ru";
    }

    @Step("Запрос создания нового пользователя")
    public static Response createUser(CreateUserBodyData bodyCreateUser){
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post(REGISTRATION_ENDPOINT);

        if (response.statusCode() == 200){
            userCreated = true;
        }
        return response;
    }
    @Step("Запрос создания нового пользователя")
    public static Response createUser(String json){
        return RestAssured
                .given()
//                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json" )
                .body(json)
                .and()
                .post(REGISTRATION_ENDPOINT);
    }



}
