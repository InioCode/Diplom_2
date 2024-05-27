import client.loginuser.LoginUserBodyData;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;

import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.createuser.CreateUser.createUser;
import static client.createuser.CreateUser.generateRandomEmail;
import static client.loginuser.LoginUser.loginUser;

public class LoginUserTest {
    LoginUserBodyData login;
    LoginUserBodyData invalidLogin;
    private String accessToken;

    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        String email = generateRandomEmail();
        String invalidMail = generateRandomEmail("InvalidEmail");
        String password = "Password";

        CreateUserBodyData bodyCreateUser = new CreateUserBodyData(email, password, "Ivan");
        accessToken = createUser(bodyCreateUser)
                .as(SuccessRegisterUserData.class)
                .getAccessToken()
                .substring(7);

        invalidLogin = new LoginUserBodyData(invalidMail, password);
        login = new LoginUserBodyData(email, password);
    }

    @Step("Выполнение удаления созданого пользователя")
    @After
    public void tearDown(){
        deleteUser(accessToken);
    }
    @DisplayName("Успешный вход возвращает код 200")
    @Test
    public void loginUser_success(){
        loginUser(login).then().log().all().statusCode(200);
    }
    @DisplayName("Вход с неверным логином возвращает код 401")
    @Test
    public void loginUserWithInvalidUserName(){
        loginUser(invalidLogin).then().statusCode(401);
    }
}
