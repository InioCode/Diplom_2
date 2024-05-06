package stellarburgers.createuser;

public class CreateUserError {
    private String success;
    private String message;

    public CreateUserError(String success, String message) {
        this.success = success;
        this.message = message;
    }

    public CreateUserError() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
