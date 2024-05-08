import client.createorder.CreateOrderBodyData;
import client.createorder.CreateOrderError;
import io.qameta.allure.Step;
import org.junit.After;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static client.DeleteUser.deleteUser;
import static client.GetIngredients.getIdIngredient;
import static client.createorder.CreateOrder.createOrderWithAuth;
import static client.createorder.CreateOrder.createOrderWithoutAuth;
import static client.createuser.CreateUser.createUser;

public class CreateOrderTest {
    private final List<String>  ingredients = new ArrayList<>();
    private final List<String> wrongIngredients = new ArrayList<>();
    private final CreateOrderBodyData createOrderBody = new CreateOrderBodyData();
    private final CreateOrderBodyData createOrderBodyWithoutIngredients = new CreateOrderBodyData();
    private final CreateOrderBodyData createOrderBodyWithWrongIngredients = new CreateOrderBodyData();
    private String email;
    private String password;
    private String accessToken;


    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        ingredients.add(getIdIngredient(0));
        ingredients.add(getIdIngredient(1));

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
    }
    @Step("Выполнение удаления созданого пользователя")
    @After
    public void tearDown(){
        deleteUser(accessToken);
    }

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
