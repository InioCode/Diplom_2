package CreateUser;

import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
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

    //    "email": "test236@mail.ru",
    //    "password": "Password",
    //    "name": "User"

    @DisplayName("Создание нового пользователя")
    @Test
    public void createUniqueUser(){
        RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post("/api/auth/register").then().log().all().statusCode(200);
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
