import client.createorder.CreateOrderBodyData;
import client.createorder.CreateOrderError;
import client.createorder.GetIngredientsData;
import io.qameta.allure.Step;
import org.junit.After;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;
import client.loginuser.LoginUserBodyData;
import client.loginuser.SuccessLoginUserData;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.createorder.CreateOrder.createOrderWithAuth;
import static client.createorder.CreateOrder.createOrderWithoutAuth;
import static client.createuser.CreateUser.createUser;

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
    private GetIngredientsData ingredientsList;

    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        //RestAssured.baseURI = BASE_URL;

//        Response response = RestAssured.given()
//                .log().all()
//                .get("/api/ingredients");

        //ingredietsList = response.as(GetIngredientsData.class);

        //String string = response.then().extract().response().asString();
        //String json = string.substring(23, string.length() - 1);

        //System.out.println(json);

        //IngredientsData[] target = new GsonBuilder().create().fromJson(json, IngredientsData[].class);
        //System.out.println();

        ingredients.add("61c0c5a71d1f82001bdaaa70");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        createOrderBody.setIngredients(ingredients);

        wrongIngredients.add("c0c5a71d1f82001bdaaa70");
        wrongIngredients.add("61c0c5a71d1f82001bdaa");
        createOrderBodyWithWrongIngredients.setIngredients(wrongIngredients);

        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        password = "Password";

        CreateUserBodyData bodyCreateUser = new CreateUserBodyData(email, password, "Ivan");
        accessToken = createUser(bodyCreateUser)
                .as(SuccessRegisterUserData.class)
                .getAccessToken()
                .substring(7);

        login = new LoginUserBodyData(email, password);
    }
    @Step("Выполнение удаления созданого пользователя")
    @After
    public void tearDown(){
        deleteUser(accessToken);
    }

//    @Test
//    public void getIngredientsList(){
//        System.out.println(ingredietsList.getSuccess());
//        System.out.println(ingredietsList.getData());
//    }

    @DisplayName("Создание заказа без аутентификации возвращает 200")
    @Test
    public void createOrderWithoutAuth_return200(){
        createOrderWithoutAuth(createOrderBody)
                .then()
                .statusCode(200);
    }

    @DisplayName("Создание заказа после пройденой аутентификации возвращает 200")
    @Test
    public void createOrderWithAuth_return200(){
        createOrderWithAuth(createOrderBody, accessToken)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Создание заказа без ингридиентов возвращает ошибку")
    @Test
    public void createOrderWithoutIngredients(){
        CreateOrderError error =  createOrderWithoutAuth(createOrderBodyWithoutIngredients).as(CreateOrderError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("Ingredient ids must be provided", error.getMessage());
    }

    @DisplayName("Создание заказа с несуществущим хэшем ингридеента возвращает код 500")
    @Test
    public void createOrderWithWrongHashIngredients(){
        createOrderWithoutAuth(createOrderBodyWithWrongIngredients)
                .then()
                .statusCode(500);

    }
}
