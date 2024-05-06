package stellarburgers.getuserorders;

import org.junit.After;
import stellarburgers.createuser.CreateUserBodyData;
import stellarburgers.createuser.SuccessRegisterUserData;
import stellarburgers.loginuser.LoginUserBodyData;
import stellarburgers.loginuser.SuccessLoginUserData;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static stellarburgers.UrlConstants.BASE_URL;

public class GetUserOrdersTest {
    private String email;
    private String password;
    private LoginUserBodyData login;
    private SuccessLoginUserData userData;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URL;

        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        password = "Password";

        CreateUserBodyData bodyCreateUser = new CreateUserBodyData(email, password, "Ivan");
        accessToken = RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post("/api/auth/register")
                .as(SuccessRegisterUserData.class)
                .getAccessToken()
                .substring(7);

        login = new LoginUserBodyData(email, password);
    }

    @After
    public void tearDown(){
            RestAssured.given()
                    .auth()
                    .oauth2(accessToken)
                    .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
    }


    @Test
    public void getUserOrdersWithAuth(){
//        userData = RestAssured.given()
//                .log().all()
//                .header("Content-type", "application/json")
//                .body(login)
//                .and()
//                .post("/api/auth/login").as(SuccessLoginUserData.class);
//
//        accessToken = userData.getAccessToken().substring(7);

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
