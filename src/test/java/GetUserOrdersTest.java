import client.getuserorders.GetUserOrders;
import client.getuserorders.GetUserOrdersError;
import org.junit.After;
import client.createuser.CreateUserBodyData;
import client.createuser.SuccessRegisterUserData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static client.DeleteUser.deleteUser;
import static client.createuser.CreateUser.createUser;

public class GetUserOrdersTest {
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp(){
        email = "test" + new Random().nextInt(1000) +"@mail.ru";
        password = "Password";

        CreateUserBodyData bodyCreateUser = new CreateUserBodyData(email, password, "Ivan");
        accessToken = createUser(bodyCreateUser)
                .as(SuccessRegisterUserData.class)
                .getAccessToken()
                .substring(7);
    }

    @After
    public void tearDown(){
            deleteUser(accessToken);
    }

    @Test
    public void getUserOrdersWithAuth(){
        GetUserOrders.getUserOrdersWithAuth(accessToken)
                .then()
                .statusCode(200);
    }

    @Test
    public void getUserOrdersWithoutAuth(){
        GetUserOrdersError error = GetUserOrders.getUserOrdersWithoutAuth().as(GetUserOrdersError.class);
        Assert.assertEquals("false", error.getSuccess());
        Assert.assertEquals("You should be authorised", error.getMessage());
    }

}
