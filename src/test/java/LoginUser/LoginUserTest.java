package LoginUser;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {
    LoginUserBodyData login;
    LoginUserBodyData invalidLogin;
    private String email;
    private String password;

    @Before
    public void setUp(){
        email = "test236@mail.ru";
        password = "Password";
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        invalidLogin = new LoginUserBodyData("InvalidEmail@mail.com", password);
        login = new LoginUserBodyData(email, password);
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
