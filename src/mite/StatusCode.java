package mite;

/**
 * HTTP status codes we support.
 */
public enum StatusCode {

    OK("200 OK"),
    NOT_IMPLEMENTED("501 Not Implemented");

    /**
     * The message that will be returned to the client for this status
     */
    private final String message;

    StatusCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
