package LoginUser;

import java.util.List;

public class SuccessLoginUserData {
    private String success;
    private String accessToken;
    private String refreshToken;
    private List<CreateUser.UserData> userData;

    public SuccessLoginUserData(String success, String accessToken, String refreshToken, List<CreateUser.UserData> userData) {
        this.success = success;
        this.userData = userData;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public SuccessLoginUserData() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CreateUser.UserData> getUserData() {
        return userData;
    }

    public void setUserData(List<CreateUser.UserData> userData) {
        this.userData = userData;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
