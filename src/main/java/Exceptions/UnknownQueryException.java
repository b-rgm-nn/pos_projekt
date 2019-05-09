package Exceptions;


public class UnknownQueryException extends Exception {

    public UnknownQueryException() {
    }
    public UnknownQueryException(String message) {
        super(message);
    }
}
