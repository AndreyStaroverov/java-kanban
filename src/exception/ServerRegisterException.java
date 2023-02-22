package exception;

public class ServerRegisterException extends RuntimeException {

    public ServerRegisterException() {
    }
    public ServerRegisterException(final String message) {
        super(message);
    }

    public ServerRegisterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServerRegisterException(final Throwable cause) {
        super(cause);
    }
}

