package exception;

public class TaskValidationException extends RuntimeException {

    public TaskValidationException() {
    }
    public TaskValidationException(final String message) {
        super(message);
    }

    public TaskValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TaskValidationException(final Throwable cause) {
        super(cause);
    }
}
