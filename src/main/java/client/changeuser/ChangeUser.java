package client.changeuser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.USER_ENDPOINT;

public class ChangeUser {
    @Step("Запрос изменения имени пользователя с прохождением аутентификации")
    public static Response changeUserNameWithAuth(String accessToken, String nameForChange){
        Map<String, String> bodyWithNewName = new HashMap<>();
        bodyWithNewName.put("name", nameForChange);

        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String json = gson.toJson(bodyWithNewName, typeObject);

        return RestAssured.given()
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(json)
                .and()
                .patch(USER_ENDPOINT);
    }
    @Step("Запрос изменения почты пользователя с прохождением аутентификации")
    public static Response changeEmailWithAuth(String accessToken, String emailForChange){
        Map<String, String> bodyWithNewEmail = new HashMap<>();
        bodyWithNewEmail.put("name", emailForChange);

        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String json = gson.toJson(bodyWithNewEmail, typeObject);

        return RestAssured.given()
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(json)
                .and()
                .patch(USER_ENDPOINT);
    }
    @Step("Запрос изменения имени пользователя без аутентификации")
    public static Response changeUserNameWithoutAuth(){
        Map<String, String> bodyWithNewName = new HashMap<>();
        bodyWithNewName.put("name", "User");

        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String json = gson.toJson(bodyWithNewName, typeObject);

       return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(json)
                .and()
                .patch(USER_ENDPOINT);
    }
}
