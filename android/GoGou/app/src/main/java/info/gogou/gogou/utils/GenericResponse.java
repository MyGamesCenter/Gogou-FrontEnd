package info.gogou.gogou.utils;

public class GenericResponse {
    private final String message;
    private final ErrorType errorType;

    public GenericResponse() {
        message = "";
        errorType = ErrorType.NONE;
    }

    public GenericResponse(final String message) {
        this(message,  ErrorType.NONE);
    }

    public GenericResponse(final String message, final ErrorType error) {
        this.message = message;
        this.errorType = error;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

}
