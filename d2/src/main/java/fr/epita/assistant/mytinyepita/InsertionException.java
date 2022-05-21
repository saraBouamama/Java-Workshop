package fr.epita.assistant.mytinyepita;

public class InsertionException extends Exception {
    public InsertionException() {
    }

    public InsertionException(String message) {
        super(message);
    }

    public InsertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertionException(Throwable cause) {
        super(cause);
    }

    public InsertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}