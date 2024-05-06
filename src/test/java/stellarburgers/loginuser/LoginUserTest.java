package stellarburgers.loginuser;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.createuser.CreateUserBodyData;
import stellarburgers.createuser.SuccessRegisterUserData;

import java.util.Random;

import static stellarburgers.UrlConstants.BASE_URL;

public class LoginUserTest {
    LoginUserBodyData login;
    LoginUserBodyData invalidLogin;
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp(){
        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        password = "Password";
        RestAssured.baseURI = BASE_URL;

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

        invalidLogin = new LoginUserBodyData("InvalidEmail@mail.com", password);
        login = new LoginUserBodyData(email, password);
    }

    @After
    public void tearDown(){
        RestAssured.given()
                .auth()
                .oauth2(accessToken)
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

    //    "email": "test236@mail.ru",
    //    "password": "Password",
    //    "name": "User"

    @Test
    public void loginUser(){
        RestAssured.given()
                .log().all()
                .header("Content-type", "application/json")
                .body(login)
                .and()
                .post("/api/auth/login").then().log().all().statusCode(200);
    }
    @Test
    public void loginUserWithInvalidUserName(){
        RestAssured.given()
                .log().all()
                .header("Content-type", "application/json")
                .body(invalidLogin)
                .and()
                .post("/api/auth/login").then().statusCode(401);
    }
}
