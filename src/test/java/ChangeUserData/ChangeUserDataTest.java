package ChangeUserData;

import LoginUser.LoginUserBodyData;
import LoginUser.SuccessLoginUserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;

public class ChangeUserDataTest {

    private String email;
    private String password;
    private LoginUserBodyData login;
    private SuccessLoginUserData userData;
    private String accessToken;

    @Before
    public void setUp(){
        email = "test236@mail.ru";
        password = "Password";
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        login = new LoginUserBodyData(email, password);
    }

    @DisplayName("Изменение имени когда аутентификация пройдена")
    @Test
    public void changeNameWithAuth(){
        userData = RestAssured.given()
                .log().all()
                .header("Content-type", "application/json")
                .body(login)
                .and()
                .post("/api/auth/login").as(SuccessLoginUserData.class);

        accessToken = userData.getAccessToken().substring(7);

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body("{\"name\": \"Test User1\"}")
                .and()
                .patch("/api/auth/user")
                .then()
                .log().all().statusCode(200);
    }

   @DisplayName("Изменение адреса почты когда аутентификация пройдена")
    @Test
    public void changeEmailWithAuth(){
        userData = RestAssured.given()
                .log().all()
                .header("Content-type", "application/json")
                .body(login)
                .and()
                .post("/api/auth/login").as(SuccessLoginUserData.class);

        accessToken = userData.getAccessToken().substring(7);

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body("{\"email\": \"test236@mail.ru\"}")
                .and()
                .patch("/api/auth/user")
                .then()
                .log().all().statusCode(200);
    }

    @DisplayName("Изменение данных без прохождения аутентификации возвращает ошибку")
    @Test
    public void changeDataWithoutAuth(){
        ChangeUserError error= RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .body("{\"name\": \"Test User1\"}")
                .and()
                .patch("/api/auth/user")
                .as(ChangeUserError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("You should be authorised", error.getMessage());
    }
}
