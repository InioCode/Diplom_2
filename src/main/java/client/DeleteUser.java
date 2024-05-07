package client;

import io.restassured.RestAssured;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.DELETE_ENDPOINT;

public class DeleteUser {
    public static void deleteUser(String accessToken){
        RestAssured.given()
                    .baseUri(BASE_URL)
                    .auth()
                    .oauth2(accessToken)
                    .delete(DELETE_ENDPOINT);
    }
}
