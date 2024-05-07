package client.getuserorders;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.ORDERS_ENDPOINT;

public class GetUserOrders {
    public static Response getUserOrdersWithAuth(String accessToken){
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .auth()
                .oauth2(accessToken)
                .header("Content-type", "application/json")
                .get(ORDERS_ENDPOINT);
    }
    public static Response getUserOrdersWithoutAuth(){
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .get(ORDERS_ENDPOINT);
    }
}
