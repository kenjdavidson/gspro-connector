package kjd.gspro.client;

public class GSProException extends Exception {

    public GSProException() {
    }

    public GSProException(String message) {
        super(message);
    }

    public GSProException(Throwable cause) {
        super(cause);
    }

    public GSProException(String message, Throwable cause) {
        super(message, cause);
    }

    public GSProException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
