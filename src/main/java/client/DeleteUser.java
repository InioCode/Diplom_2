package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.DELETE_ENDPOINT;

public class DeleteUser {
    @Step("Запрос удаления пользоваетля")
    public static void deleteUser(String accessToken){
        RestAssured.given()
                    .baseUri(BASE_URL)
                    .auth()
                    .oauth2(accessToken)
                    .delete(DELETE_ENDPOINT);
    }
}
