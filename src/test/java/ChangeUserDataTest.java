import client.changeuser.ChangeUserError;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.changeuser.ChangeUser.*;
import static client.createuser.CreateUser.createUser;
import static client.createuser.CreateUser.generateRandomEmail;

public class ChangeUserDataTest {

    private String email;
    private String emailForChange;
    private String password;
    private String accessToken;

    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        int randInt = new Random().nextInt(1000);
        email = generateRandomEmail();
        emailForChange = generateRandomEmail("change");
        password = "Password";

        CreateUserBodyData bodyCreateUser = new CreateUserBodyData(email, password, "Ivan");

        accessToken = createUser(bodyCreateUser)
                .as(SuccessRegisterUserData.class)
                .getAccessToken()
                .substring(7);

        //System.out.println(email);
    }
    @Step("Выполнение удаления созданого пользователя")
    @After
    public void tearDown(){
        deleteUser(accessToken);
    }

    @DisplayName("Изменение имени когда аутентификация пройдена возвращает код 200")
    @Test
    public void changeNameWithAuth_return200(){
        changeUserNameWithAuth(accessToken, "New name")
                .then()
                .log().all().statusCode(200);
    }

   @DisplayName("Изменение адреса почты когда аутентификация пройдена возвращает код 200")
    @Test
    public void changeEmailWithAuth_return200(){
       changeEmailWithAuth(accessToken ,emailForChange)
                .then()
                .log().all().statusCode(200);
    }

    @DisplayName("Изменение данных без прохождения аутентификации возвращает ошибку")
    @Test
    public void changeDataWithoutAuth(){
        ChangeUserError error= changeUserNameWithoutAuth().as(ChangeUserError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("You should be authorised", error.getMessage());
    }
}
