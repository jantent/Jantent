package springboot.exception;

/**
 * @author tangj
 * @date 2018/1/21 14:05
 */
public class TipException extends RuntimeException {
    public TipException() {
    }

    public TipException(String message) {
        super(message);
    }

    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipException(Throwable cause) {
        super(cause);
    }
}
