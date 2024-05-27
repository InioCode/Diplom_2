package client.loginuser;

public class LoginUserBodyData {
    private String email;
    private String password;

    public LoginUserBodyData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginUserBodyData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
