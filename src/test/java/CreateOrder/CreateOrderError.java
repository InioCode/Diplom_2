package CreateOrder;

public class CreateOrderError {
    private String success;
    private String message;

    public CreateOrderError(String success, String message) {
        this.success = success;
        this.message = message;
    }

    public CreateOrderError() {
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
