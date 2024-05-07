package client.createorder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.ORDERS_ENDPOINT;

public class CreateOrder {
    public static Response createOrderWithoutAuth(CreateOrderBodyData createOrderBody){
        return RestAssured
                .given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(createOrderBody)
                .post(ORDERS_ENDPOINT);
    }

    public static Response createOrderWithAuth(CreateOrderBodyData createOrderBody, String accessToken){
        return RestAssured
                .given()
                .log().all()
                .baseUri(BASE_URL)
                .auth()
                .oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(createOrderBody)
                .post(ORDERS_ENDPOINT);
    }
}
