import client.createuser.CreateUserBodyData;
import client.createuser.CreateUserError;
import client.createuser.SuccessRegisterUserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static client.DeleteUser.deleteUser;
import static client.createuser.CreateUser.*;

public class CreateUserTest {

    private CreateUserBodyData userBodyData;
    private String email;
    private String userName;
    private String password;
    private String accessToken;

    @Step("Выполение создания нового пользователя")
    @Before
    public void setUp(){
        email = generateRandomEmail();
        userName = "User";
        password = "Password";

        userBodyData = new CreateUserBodyData(email, password, userName);
    }

    @Step("Выполнение удаления созданого пользователя")
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
        //userCreated = true;
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);
    }

    @DisplayName("Создание нового пользователя с уже зарегестрированной почтой возвращает код 403")
    @Test
    public void createNonUniqueUserReturn403(){
        Response response = createUser(userBodyData);
        accessToken = response.as(SuccessRegisterUserData.class).getAccessToken().substring(7);

        createUser(userBodyData).then().statusCode(403);
    }

    @DisplayName("Создание пользователя без одного поля возвращает код 403")
    @Test
    public void createUserWithoutOneFieldReturn403(){
        Map<String, String> userBodyWithoutOneField = new HashMap<>();
        userBodyWithoutOneField.put("email", "test@mail.ru");
        userBodyWithoutOneField.put("password", "Password");

        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String json = gson.toJson(userBodyWithoutOneField, typeObject);

        CreateUserError error = createUser(json).as(CreateUserError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("Email, password and name are required fields", error.getMessage());
    }
}
