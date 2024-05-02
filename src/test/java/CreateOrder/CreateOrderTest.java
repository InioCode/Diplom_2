package CreateOrder;

import LoginUser.LoginUserBodyData;
import LoginUser.SuccessLoginUserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest {
    private List<String> ingredients = new ArrayList<>();
    private List<String> wrongIngredients = new ArrayList<>();
    private CreateOrderBodyData createOrderBody = new CreateOrderBodyData();
    private CreateOrderBodyData createOrderBodyWithoutIngredients = new CreateOrderBodyData();
    private CreateOrderBodyData createOrderBodyWithWrongIngredients = new CreateOrderBodyData();
    private String email;
    private String password;
    private LoginUserBodyData login;
    private SuccessLoginUserData userData;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        ingredients.add("61c0c5a71d1f82001bdaaa70");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        createOrderBody.setIngredients(ingredients);

        wrongIngredients.add("c0c5a71d1f82001bdaaa70");
        wrongIngredients.add("61c0c5a71d1f82001bdaa");
        createOrderBodyWithWrongIngredients.setIngredients(wrongIngredients);

        email = "test236@mail.ru";
        password = "Password";
        login = new LoginUserBodyData(email, password);
    }

    @DisplayName("Создание заказа без аутентификации")
    @Test
    public void createOrderWithoutAuth(){
        RestAssured
                .given().log().all()
                .header("Content-type", "application/json")
                .body(createOrderBody)
                .post("/api/orders")
                .then().log().all()
                .statusCode(200);

    }

    @DisplayName("Создание заказа после пройденой аутентификации")
    @Test
    public void createOrderWithAuth(){
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
                .body(createOrderBody)
                .post("/api/orders")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Создание заказа без ингридиентов возвращает ошибку")
    @Test
    public void createOrderWithoutIngredients(){
        CreateOrderError error =  RestAssured
                .given().log().all()
                .header("Content-type", "application/json")
                .body(createOrderBodyWithoutIngredients)
                .post("/api/orders")
                .as(CreateOrderError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("Ingredient ids must be provided", error.getMessage());

    }

    @DisplayName("Создание заказа с несуществущим хэшем ингридеента возвращает код 500")
    @Test
    public void createOrderWithWrongHashIngredients(){
        RestAssured
                .given().log().all()
                .header("Content-type", "application/json")
                .body(createOrderBodyWithWrongIngredients)
                .post("/api/orders")
                .then().log().all()
                .statusCode(500);

    }
}
