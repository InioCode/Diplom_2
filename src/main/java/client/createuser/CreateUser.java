package client.createuser;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.REGISTRATION_ENDPOINT;

public class CreateUser {

    public static Response createUser(CreateUserBodyData bodyCreateUser){
        return RestAssured
                .given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post(REGISTRATION_ENDPOINT);
    }

    public static Response createUser(String json){
        return RestAssured
                .given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json" )
                .body(json)
                .and()
                .post(REGISTRATION_ENDPOINT);
    }



}
