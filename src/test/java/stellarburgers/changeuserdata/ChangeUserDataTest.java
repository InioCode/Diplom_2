package stellarburgers.changeuserdata;

import io.restassured.response.Response;
import stellarburgers.createuser.CreateUserBodyData;
import stellarburgers.createuser.SuccessRegisterUserData;
import stellarburgers.loginuser.LoginUserBodyData;
import stellarburgers.loginuser.SuccessLoginUserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;

import java.util.Random;

import static stellarburgers.UrlConstants.BASE_URL;

public class ChangeUserDataTest {

    private String email;
    private String emailForChange;
    private String password;
    private LoginUserBodyData login;
    private SuccessLoginUserData userData;
    private String accessToken;

    @Before
    public void setUp(){
        int randInt = new Random().nextInt(1000);
        email = "test" + randInt + "@mail.ru";
        emailForChange = "change" + randInt + "@mail.ru";
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

        login = new LoginUserBodyData(email, password);
        System.out.println(email);
    }

    @After
    public void tearDown(){
        RestAssured.given()
                .auth()
                .oauth2(accessToken)
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
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

//        accessToken = userData.getAccessToken().substring(7);

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

        //accessToken = userData.getAccessToken().substring(7);

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body("{\"email\": \"" + emailForChange + "\"}")
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
