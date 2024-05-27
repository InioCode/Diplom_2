package client.loginuser;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.LOGIN_ENDPOINT;

public class LoginUser {
    public static Response loginUser(LoginUserBodyData login){
        return RestAssured.given()
                .baseUri(BASE_URL)
                .log().all()
                .header("Content-type", "application/json")
                .body(login)
                .and()
                .post(LOGIN_ENDPOINT);
    }
}
