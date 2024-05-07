import client.createuser.CreateUserBodyData;
import client.createuser.CreateUserError;
import client.createuser.SuccessRegisterUserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.createuser.CreateUser.createUser;

public class CreateUserTest {

    private CreateUserBodyData userBodyData;
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

        userBodyData = new CreateUserBodyData(email, password, userName);
    }

    @After
    public void tearDown() {
        if (userCreated){
            deleteUser(accessToken);
        }
    }

    @DisplayName("Создание нового пользователя")
    @Test
    public void createUniqueUser_success(){
        Response response = createUser(userBodyData);
        response.then().statusCode(200);
        userCreated = true;
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);
    }

    @DisplayName("Создание нового пользователя с уже зарегестрированной почтой возвращает код 403")
    @Test
    public void createNonUniqueUserReturn403(){
        Response response = createUser(userBodyData);
        userCreated =true;
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);

        createUser(userBodyData).then().statusCode(403);
    }

    @DisplayName("Создание пользователя без одного поля возвращает код 403")
    @Test
    public void createUserWithoutOneFieldReturn403(){
        String userBodyWithoutOneField = "{\"email\":\"test@mail.ru\",\"password\":\"User\"}";
        CreateUserError error = createUser(userBodyWithoutOneField).as(CreateUserError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("Email, password and name are required fields", error.getMessage());
    }
}
