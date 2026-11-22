public class InvalidFieldValueException extends IllegalArgumentException {

    public InvalidFieldValueException(String message) {
        super(message);
    }

    public InvalidFieldValueException(String message, Throwable cause) {
        super(message, cause);
    }
}