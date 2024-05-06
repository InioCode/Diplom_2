package stellarburgers.createuser;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static stellarburgers.UrlConstants.BASE_URL;

public class CreateUserTest {

    private CreateUserBodyData bodyCreateUser;
    private CreateUserBodyData bodyNonUniqueUser;
    private String email;
    private String userName;
    private String password;
    private String accessToken;
    private boolean userCreated = false;



    @Before
    public void setUp(){
        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        userName = "User";
        password = "Password";
        System.out.println(email);

        RestAssured.baseURI = BASE_URL;
        bodyCreateUser = new CreateUserBodyData(email, password, userName);
    }

    @After
    public void tearDown() {
        if (userCreated){
            RestAssured.given()
                    .auth()
                    .oauth2(accessToken)
                    .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
        }
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
        userCreated = true;
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);
    }

    @DisplayName("Создание нового пользователя с уже зарегестрированной почтой возвращает код 403")
    @Test
    public void createNonUniqueUserReturn403(){
        Response response = RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
                .and()
                .post("/api/auth/register");
        userCreated =true;
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);

        RestAssured
                .given().log().all()
                .header("Content-type", "application/json" )
                .body(bodyCreateUser)
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
