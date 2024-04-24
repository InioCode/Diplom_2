package GetUserOrders;

import LoginUser.LoginUserBodyData;
import LoginUser.SuccessLoginUserData;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetUserOrders {
    private String email;
    private String password;
    private LoginUserBodyData login;
    private SuccessLoginUserData userData;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        email = "test236@mail.ru";
        password = "Password";
        login = new LoginUserBodyData(email, password);
    }

    @Test
    public void getUserOrdersWithAuth(){
        userData = RestAssured.given()
                .log().all()
                .header("Content-type", "application/json")
                .body(login)
                .and()
                .post("/api/auth/login").as(SuccessLoginUserData.class);

        accessToken = userData.getAccessToken().substring(7);

        RestAssured
                .given().log().all()
                .auth()
                .oauth2(accessToken)
                .header("Content-type", "application/json")
                .get("/api/orders")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    public void getUserOrdersWithoutAuth(){
        GetUserOrdersError error = RestAssured
                .given().log().all()
                .header("Content-type", "application/json")
                .get("/api/orders")
                .as(GetUserOrdersError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("You should be authorised", error.getMessage());
    }

}
