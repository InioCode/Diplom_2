package client.changeuser;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.USER_ENDPOINT;

public class ChangeUser {
    public static Response changeUserNameWithAuth(String accessToken, String nameForChange){
        return RestAssured.given()
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body("{\"name\": \"" + nameForChange + "\"}")
                .and()
                .patch(USER_ENDPOINT);
    }

    public static Response changeEmailWithAuth(String accessToken, String emailForChange){
        return RestAssured.given()
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body("{\"email\": \"" + emailForChange + "\"}")
                .and()
                .patch(USER_ENDPOINT);
    }

    public static Response changeUserNameWithoutAuth(){
       return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body("{\"name\": \"Test User1\"}")
                .and()
                .patch(USER_ENDPOINT);
    }
}
