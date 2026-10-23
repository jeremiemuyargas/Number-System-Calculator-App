/**
 * Thrown when a user enters a value that contains a digit
 * not valid for the selected number system (e.g. "9" in Binary).
 */
public class InvalidDigitException extends Exception {
    public InvalidDigitException(String message) {
        super(message);
    }
}
