package CreateUser;

import LoginUser.SuccessLoginUserData;
import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class CreateUserTest {

    private CreateUserBodyData bodyCreateUser;
    private CreateUserBodyData bodyNonUniqueUser;
    private String email;
    private String userName;
    private String password;
    private String accessToken;



    @Before
    public void setUp(){
        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        userName = "User";
        password = "Password";
        System.out.println(email);

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        bodyCreateUser = new CreateUserBodyData(email, password, userName);
        bodyNonUniqueUser = new CreateUserBodyData("test236@mail.ru", password, userName);
    }

    @After
    public void tearDown() {
        accessToken = accessToken.substring(7);

        RestAssured.given()
                .auth()
                .oauth2(accessToken)
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

    @DisplayName("Создание нового пользователя")
    @Test
    public void createUniqueUser(){
        Response response = RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post("/api/auth/register");
        response.then().statusCode(200);

        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken();

    }

    @DisplayName("Создание нового пользователя с уже зарегестрированной почтой возвращает код 403")
    @Test
    public void createNonUniqueUserReturn403(){
        RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyNonUniqueUser)
                .and()
                .post("/api/auth/register").then().log().all().statusCode(403);
    }

    @DisplayName("Создание пользователя без одного поля возвращает код 403")
    @Test
    public void createUserWithoutOneFieldReturn403(){
        String UserBodyWithoutOneField = "{\"email\":\"test@mail.ru\",\"password\":\"User\"}";
        CreateUserError error = RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(UserBodyWithoutOneField)
                .and()
                .post("/api/auth/register").as(CreateUserError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("Email, password and name are required fields", error.getMessage());
    }
}
