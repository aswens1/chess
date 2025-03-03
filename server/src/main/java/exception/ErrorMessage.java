package exception;

public class ErrorMessage {

    final int statusCode;
    final String message;

    public ErrorMessage(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
