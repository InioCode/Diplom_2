import client.getuserorders.GetUserOrders;
import client.getuserorders.GetUserOrdersError;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.createuser.CreateUser.createUser;
import static client.createuser.CreateUser.generateRandomEmail;

public class GetUserOrdersTest {
    private String email;
    private String password;
    private String accessToken;

    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        email = generateRandomEmail();
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

    @DisplayName("Запрос заказов с аутантификацие возвращает код 200")
    @Test
    public void getUserOrdersWithAuth(){
        GetUserOrders.getUserOrdersWithAuth(accessToken)
                .then()
                .statusCode(200);
    }

    @DisplayName("Запрос заказов с аутантификацие возвращает код 200")
    @Test
    public void getUserOrdersWithoutAuth(){
        GetUserOrdersError error = GetUserOrders.getUserOrdersWithoutAuth().as(GetUserOrdersError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("You should be authorised", error.getMessage());
    }

}
